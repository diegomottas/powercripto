package br.com.powercripto.web.rest;

import br.com.powercripto.PowercriptoApp;
import br.com.powercripto.domain.Cripto;
import br.com.powercripto.repository.CriptoRepository;
import br.com.powercripto.service.CriptoService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CriptoResource REST controller.
 *
 * @see CriptoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PowercriptoApp.class)
@WebAppConfiguration
@IntegrationTest
public class CriptoResourceIntTest {


    private static final Long DEFAULT_QUANTIDADE_HASHES = 1L;
    private static final Long UPDATED_QUANTIDADE_HASHES = 2L;

    private static final BigDecimal DEFAULT_TEMPO = new BigDecimal(1);
    private static final BigDecimal UPDATED_TEMPO = new BigDecimal(2);

    @Inject
    private CriptoRepository criptoRepository;

    @Inject
    private CriptoService criptoService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCriptoMockMvc;

    private Cripto cripto;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CriptoResource criptoResource = new CriptoResource();
        ReflectionTestUtils.setField(criptoResource, "criptoService", criptoService);
        this.restCriptoMockMvc = MockMvcBuilders.standaloneSetup(criptoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        cripto = new Cripto();
        cripto.setQuantidadeHashes(DEFAULT_QUANTIDADE_HASHES);
        cripto.setTempo(DEFAULT_TEMPO);
    }

    @Test
    @Transactional
    public void createCripto() throws Exception {
        int databaseSizeBeforeCreate = criptoRepository.findAll().size();

        // Create the Cripto

        restCriptoMockMvc.perform(post("/api/criptos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cripto)))
                .andExpect(status().isCreated());

        // Validate the Cripto in the database
        List<Cripto> criptos = criptoRepository.findAll();
        assertThat(criptos).hasSize(databaseSizeBeforeCreate + 1);
        Cripto testCripto = criptos.get(criptos.size() - 1);
        assertThat(testCripto.getQuantidadeHashes()).isEqualTo(DEFAULT_QUANTIDADE_HASHES);
//        assertThat(testCripto.getTempo()).isEqualTo(DEFAULT_TEMPO);
    }

    @Test
    @Transactional
    public void getAllCriptos() throws Exception {
        // Initialize the database
        criptoRepository.saveAndFlush(cripto);

        // Get all the criptos
        restCriptoMockMvc.perform(get("/api/criptos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cripto.getId().intValue())))
                .andExpect(jsonPath("$.[*].quantidadeHashes").value(hasItem(DEFAULT_QUANTIDADE_HASHES.intValue())))
                .andExpect(jsonPath("$.[*].tempo").value(hasItem(DEFAULT_TEMPO.intValue())));
    }

    @Test
    @Transactional
    public void getCripto() throws Exception {
        // Initialize the database
        criptoRepository.saveAndFlush(cripto);

        // Get the cripto
        restCriptoMockMvc.perform(get("/api/criptos/{id}", cripto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(cripto.getId().intValue()))
            .andExpect(jsonPath("$.quantidadeHashes").value(DEFAULT_QUANTIDADE_HASHES.intValue()))
            .andExpect(jsonPath("$.tempo").value(DEFAULT_TEMPO.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCripto() throws Exception {
        // Get the cripto
        restCriptoMockMvc.perform(get("/api/criptos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCripto() throws Exception {
        // Initialize the database
        criptoService.save(cripto);

        int databaseSizeBeforeUpdate = criptoRepository.findAll().size();

        // Update the cripto
        Cripto updatedCripto = new Cripto();
        updatedCripto.setId(cripto.getId());
        updatedCripto.setQuantidadeHashes(UPDATED_QUANTIDADE_HASHES);
        updatedCripto.setTempo(UPDATED_TEMPO);

        restCriptoMockMvc.perform(put("/api/criptos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCripto)))
                .andExpect(status().isOk());

        // Validate the Cripto in the database
        List<Cripto> criptos = criptoRepository.findAll();
        assertThat(criptos).hasSize(databaseSizeBeforeUpdate);
        Cripto testCripto = criptos.get(criptos.size() - 1);
        assertThat(testCripto.getQuantidadeHashes()).isEqualTo(UPDATED_QUANTIDADE_HASHES);
//        assertThat(testCripto.getTempo()).isEqualTo(UPDATED_TEMPO);
    }

    @Test
    @Transactional
    public void deleteCripto() throws Exception {
        // Initialize the database
        criptoService.save(cripto);

        int databaseSizeBeforeDelete = criptoRepository.findAll().size();

        // Get the cripto
        restCriptoMockMvc.perform(delete("/api/criptos/{id}", cripto.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Cripto> criptos = criptoRepository.findAll();
        assertThat(criptos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
