package com.daniel.springcloud.msvc.items.domain.port.in;

import java.util.List;

import com.daniel.springcloud.msvc.items.application.utils.ResponseGenericObject;
import com.daniel.springcloud.msvc.items.domain.model.Item;

import reactor.core.publisher.Mono;

public interface ItemUseCase {
    
    Mono<ResponseGenericObject<List<Item>>> findAll();
    Mono<ResponseGenericObject<Item>> findById(long id);

}
