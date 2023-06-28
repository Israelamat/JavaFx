package fx.javafx;

import javafx.collections.ObservableList;
import org.junit.Before;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

public class MainControllerTest {
    private MainController mainController;

    @Before
    public void setUp() {
        mainController = new MainController();
    }

    @Test
    public void testFillOpt() {
        String[] fields = {"question", "opt1", "opt2", "opt3", "opt4", "correctAnswer"};

        List<Option> options = mainController.fillOpt(fields);
        assertEquals(4, options.size());
    }

    @Test
    public void testLoadListViewData() {
        ObservableList<Question> questionsObservableList = mainController.loadListViewData();
        assertNotNull(questionsObservableList);
    }

    @Test
    public void testFillTextFieldsWithCurrentQuestion() {
        mainController.questionList.getSelectionModel().select(0);

        mainController.fillTextFieldsWithCurrentQuestion();

        assertNotNull(mainController.question.getText());
        assertNotNull(mainController.opt1.getText());
        assertNotNull(mainController.opt2.getText());
        assertNotNull(mainController.opt3.getText());
        assertNotNull(mainController.opt4.getText());
    }
}
