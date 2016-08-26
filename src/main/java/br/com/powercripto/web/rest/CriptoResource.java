package br.com.powercripto.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.powercripto.domain.Cripto;
import br.com.powercripto.service.CriptoService;
import br.com.powercripto.web.rest.util.HeaderUtil;
import br.com.powercripto.web.rest.util.PaginationUtil;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Cripto.
 */
@RestController
@RequestMapping("/api")
public class CriptoResource {

    private final Logger log = LoggerFactory.getLogger(CriptoResource.class);

    @Inject
    private CriptoService criptoService;

    /**
     * POST  /criptos : Create a new cripto.
     *
     * @param cripto the cripto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cripto, or with status 400 (Bad Request) if the cripto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/criptos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cripto> createCripto(@RequestBody Cripto cripto) throws URISyntaxException {
        log.debug("REST request to save Cripto : {}", cripto);
        if (cripto.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cripto", "idexists", "A new cripto cannot already have an ID")).body(null);
        }
        Cripto result = criptoService.save(cripto);
        return ResponseEntity.created(new URI("/api/criptos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cripto", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /criptos : Updates an existing cripto.
     *
     * @param cripto the cripto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cripto,
     * or with status 400 (Bad Request) if the cripto is not valid,
     * or with status 500 (Internal Server Error) if the cripto couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/criptos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cripto> updateCripto(@RequestBody Cripto cripto) throws URISyntaxException {
        log.debug("REST request to update Cripto : {}", cripto);
        if (cripto.getId() == null) {
            return createCripto(cripto);
        }
        Cripto result = criptoService.save(cripto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cripto", cripto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /criptos : get all the criptos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of criptos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/criptos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Cripto>> getAllCriptos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Criptos");
        Page<Cripto> page = criptoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/criptos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /criptos/:id : get the "id" cripto.
     *
     * @param id the id of the cripto to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cripto, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/criptos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cripto> getCripto(@PathVariable Long id) {
        log.debug("REST request to get Cripto : {}", id);
        Cripto cripto = criptoService.findOne(id);
        return Optional.ofNullable(cripto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /criptos/:id : delete the "id" cripto.
     *
     * @param id the id of the cripto to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/criptos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCripto(@PathVariable Long id) {
        log.debug("REST request to delete Cripto : {}", id);
        criptoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cripto", id.toString())).build();
    }

}
