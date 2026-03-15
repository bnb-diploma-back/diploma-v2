package sdu.edu.kz.diploma.library.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BaseTest {

    @Autowired
    public Creator creator;

    @Autowired
    public Editor editor;

    @Autowired
    public Remover remover;

    @Autowired
    public Randomizer randomizer;
}