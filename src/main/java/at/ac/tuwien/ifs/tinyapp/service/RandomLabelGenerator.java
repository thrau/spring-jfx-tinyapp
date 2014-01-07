package at.ac.tuwien.ifs.tinyapp.service;

import java.util.Random;

import org.springframework.stereotype.Service;

/**
 * An implementation of a LabelGenerator that selects a random label from a predefined list.
 */
@Service
public class RandomLabelGenerator implements LabelGenerator {

    public static final String[] LABELS = {
        "Your commit is writing checks your merge can't cash.",
        "Things went wrong...",
        "I'm too foo for this bar",
        "You can't see it, but I'm making a very angry face right now",
        "Become a programmer, they said. It'll be fun, they said.",
        "It compiles! Ship it!",
        "For great justice.",
        "Who has two thumbs and remembers the rudiments of his linear algebra courses?"
    };

    public Random rng = new Random();

    @Override
    public String generate() {
        return getLabel(randomLabelIndex());
    }

    private int randomLabelIndex() {
        return rng.nextInt(LABELS.length);
    }

    private String getLabel(int index) {
        return LABELS[index];
    }

}
