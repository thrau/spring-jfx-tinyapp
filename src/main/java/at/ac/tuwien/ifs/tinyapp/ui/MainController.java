package at.ac.tuwien.ifs.tinyapp.ui;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ac.tuwien.ifs.tinyapp.service.LabelGenerator;

/**
 * The Main Controller.
 */
@Component
public class MainController {

    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private LabelGenerator labelGenerator;

    @FXML
    private Text label;

    @FXML
    public void initialize() {
        LOG.debug("init {} with generator {}", getClass(), labelGenerator);
        regenerate();
    }

    @FXML
    void regenerate() {
        if (labelGenerator != null) {
            label.setText(labelGenerator.generate());
        } else {
            label.setText("OH NOES NULL");
        }
    }

    public void setLabelGenerator(LabelGenerator labelGenerator) {
        this.labelGenerator = labelGenerator;
    }
}
