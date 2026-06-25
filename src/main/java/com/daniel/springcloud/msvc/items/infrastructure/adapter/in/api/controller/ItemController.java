package com.daniel.springcloud.msvc.items.infrastructure.adapter.in.api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.daniel.springcloud.msvc.items.application.utils.ResponseGenericObject;
import com.daniel.springcloud.msvc.items.domain.model.Item;
import com.daniel.springcloud.msvc.items.domain.model.Product;
import com.daniel.springcloud.msvc.items.domain.port.in.ItemUseCase;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemUseCase useCase;

    private final CircuitBreakerFactory cBreakerFactory;

    private final Logger logger = LoggerFactory.getLogger(ItemController.class);
    private final Logger loggerFactory = LoggerFactory.getLogger(ItemController.class);

    @GetMapping("")
    public ResponseEntity<ResponseGenericObject<List<Item>>> getAllItems() {
        return ResponseEntity.ok().body(useCase.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseGenericObject<Optional<Item>>> getItemById(@PathVariable("id") Long id, @RequestParam(name="name") String name
            , @RequestHeader(name="token-request") String token_request) {
        //logger.info("Parametro inyectado: name = " + name);
        //logger.info("Header inyectado: token-request = " + token_request);        
        ResponseGenericObject<Optional<Item>> response = cBreakerFactory.create("items").run(
            () -> useCase.findById(id)
            , e -> { 
                //En este espacio se puede hacer el llamado a otro micro servicio si es el caso
                //aca lo que se hace es el camino alterno cuando el circuito se abra
                //Para este caso se devuelve un producto generico
                //-------------------------------------------------------------------------------
                loggerFactory.error(e.getMessage());

                Product product = new Product();
                product.setCreateAt(new Date());
                product.setId(999L);
                product.setName("Camera Sony");
                product.setPort(8888);
                product.setPrice(9999.99);
                product.setStock(0);

                Item item = new Item(product, 0);
                return new ResponseGenericObject<>(
                    false
                    , true
                    , e.getMessage()
                    , Optional.of(item) //Importante el Optional.of, ya que se devuelve un Optional de Item, si se pone Item solo, falla
                );
                //-------------------------------------------------------------------------------
                //En este espacio se puede hacer el llamado a otro micro servicio si es el caso
                //aca lo que se hace es el camino alterno cuando el circuito se abra                
            });

        return ResponseEntity.ok().body(response);
    }
    
    
    
}
