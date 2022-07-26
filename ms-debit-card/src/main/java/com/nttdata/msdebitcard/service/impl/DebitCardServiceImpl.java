package com.nttdata.msdebitcard.service.impl;

import com.nttdata.msdebitcard.client.AccountClient;
import com.nttdata.msdebitcard.model.Account;
import com.nttdata.msdebitcard.model.DebitCard;
import com.nttdata.msdebitcard.repository.DebitCardRepository;
import com.nttdata.msdebitcard.service.DebitCardService;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class DebitCardServiceImpl implements DebitCardService {
    @Autowired
    DebitCardRepository repository;

    @Autowired
    AccountClient accountClient;

    @Override
    public Flux<DebitCard> accountDetail(String cardNumber) {

        return findAll().filter(dc -> dc.getCardNumber().equalsIgnoreCase(cardNumber))
                .flatMap(debitCard -> accountClient.findAllWithDetail()
                        .filter(a -> a.getCardNumber().equalsIgnoreCase(cardNumber) && a.getCardNumber().equalsIgnoreCase(debitCard.getCardNumber()))
                        .collectList()
                        .flatMapMany(a -> {
                            ValorAllValidator(debitCard, a);
                            return Flux.just(debitCard);
                        }));

    }

    private void ValorAllValidator(DebitCard debitCard, List<Account> account) {
        account.sort((o1, o2) -> o1.getCreditCardAssociationDate().compareTo(o2.getCreditCardAssociationDate()));
        debitCard.setAccount(account);
    }

    @Override
    public Flux<DebitCard> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<DebitCard> create(DebitCard dc) {
        return repository.save(dc);
    }

    @Override
    public Mono<DebitCard> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<DebitCard> update(DebitCard dc, String id) {
        return repository.findById(id)
                .map( x -> {
                    x.setCardNumber(dc.getCardNumber());
                    return x;
                }).flatMap(repository::save);
    }

    @Override
    public Mono<Void> delete(String id) {
        return repository.deleteById(id);
    }

    @Override
    public Flux<DebitCard> principalDebitAccount(String cardNumber) {
        return findAll().filter(dc -> dc.getCardNumber().equalsIgnoreCase(cardNumber))
                .flatMap(debitCard -> accountClient.findAllWithDetail()
                        .filter(trans -> trans.getCardNumber().equalsIgnoreCase(cardNumber) && trans.getCardNumber().equalsIgnoreCase(debitCard.getCardNumber()))
                        .collectList()
                        .flatMapMany(trans -> {
                            trans.sort((o1, o2) -> o1.getCreditCardAssociationDate().compareTo(o2.getCreditCardAssociationDate()));
                            Account otrans = trans.stream().filter(t -> t.getProduct().getIndProduct() == 2 ).findFirst().get();
                            debitCard.setTrans(otrans);
                            return Flux.just(debitCard);
                        }));
    }
}
