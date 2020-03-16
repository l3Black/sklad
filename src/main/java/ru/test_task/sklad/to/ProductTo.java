package ru.test_task.sklad.to;

public class ProductTo {
    private final Long article;
    private final String name;
    private final Integer balance;

    public ProductTo(Long article, String name, Integer balance) {
        this.article = article;
        this.name = name;
        this.balance = balance;
    }
}
