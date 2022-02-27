package Rlamp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.iit.cs445.spring2022.restlamp.Lamp;

public class LampTest {
    Lamp l;

    @BeforeEach
    public void setUp() throws Exception {
        l = new Lamp();
    }

    @Test
    public void test_new_default_lamp_is_off() {
        assertFalse(l.isOn());
    }

    @Test
    public void test_new_default_lamp_can_be_turned_on() {
        l.turnOn();
        assertTrue(l.isOn());
    }

    @Test
    public void test_turn_on_and_off_new_default_lamp() {
        l.turnOn();
        l.turnOff();
        assertFalse(l.isOn());
    }

    @Test
    public void test_twice_on_leaves_the_lamp_on_for_new_default_lamp() {
        l.turnOn();
        l.turnOn();
        assertTrue(l.isOn());
    }

    @Test
    public void test_twice_off_leaves_the_lamp_off_for_new_default_lamp() {
        l.turnOff();
        l.turnOff();
        assertFalse(l.isOn());
    }
}

