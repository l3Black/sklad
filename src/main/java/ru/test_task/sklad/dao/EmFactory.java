package ru.test_task.sklad.dao;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EmFactory {
    public static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("ru.test_task.sklad");
}
