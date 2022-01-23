package com.example.spring.mongodb.mongorepository;

import com.example.spring.mongodb.mongorepository.dto.Person;
import com.example.spring.mongodb.mongorepository.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@SpringBootApplication
public class MongoRepositoryApplication implements CommandLineRunner
{
    // ============================== [Fields] ==============================

    // -------------------- [Private Fields] --------------------

    Logger logger = LoggerFactory.getLogger(MongoRepositoryApplication.class);

    @Autowired
    private PersonRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    // ============================== [Methods] ==============================

    // -------------------- [Private Methods] --------------------

    // -------------------- [Public Methods] --------------------

    public static void main(String[] args)
    {
        SpringApplication.run(MongoRepositoryApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        Person person = new Person("Joe", 34);

        // Delete all.
        this.repository.deleteAll();

        // Insert is used to initially store the object into the database.
        this.repository.save(person);
        logger.info("Insert: " + person);

        // Find by ID.
        Optional<Person> personOptional = this.repository.findById(person.getId());
        logger.info("Found by ID: " + personOptional.get());

        // Find by name.
        personOptional = this.repository.findByName(person.getName());
        logger.info("Found by name: " + personOptional.get());

        // Find by age.
        personOptional = this.repository.findByAge(person.getAge());
        logger.info("Found by age: " + personOptional.get());

        // Update
        personOptional = this.repository.findById(person.getId());
        Person personUpdate = personOptional.get();
        personUpdate.setAge(45);
        this.repository.save(personUpdate);

        personOptional = this.repository.findById(person.getId());
        logger.info("Updated: " + personOptional.get());

        // Delete
        this.repository.deleteById(person.getId());

        // Check that deletion worked
        List<Person> people = this.repository.findAll();
        logger.info("Number of people = : " + people.size());

        // Drop the collection.
        mongoTemplate.dropCollection(Person.class);
    }
}
