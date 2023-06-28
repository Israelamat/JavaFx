package fx.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MainController implements Initializable {
    @FXML public TitledPane titles;
    @FXML public TextArea question;
    @FXML public TextField opt1;
    @FXML public TextField opt2;
    @FXML public TextField opt3;
    @FXML public TextField opt4;
    @FXML public Button bt_Opt1;
    @FXML public Button bt_Opt4;
    @FXML public Button bt_Opt2;
    @FXML public Button bt_Opt3;
    @FXML public TitledPane title_NewQuestion;
    @FXML public TextArea question1;
    @FXML public TextField opt11;
    @FXML public TextField opt21;
    @FXML public TextField opt31;
    @FXML public TextField opt41;
    @FXML public Button bt_AddQuestion;
    @FXML public ListView questionList;
    @FXML public Label lb_Correct;
    @FXML public TextField correctAnswer;
    @FXML public TextArea ta_Score;
    @FXML public Label lb_score;
    @FXML public Label lb_Score;
    ObservableList<Question> questionsObservableList;
    boolean stillPlaying = false;
    int score = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questionsObservableList = loadListViewData();
    }

    public List<Option> fillOpt(String[] fields){
        List<Option> options = new ArrayList<>();
        Option op1 = new Option(fields[1]);
        Option op2 = new Option(fields[2]);
        Option op3 = new Option(fields[3]);
        Option op4 = new Option(fields[4]);

        options.add(op1);
        options.add(op2);
        options.add(op3);
        options.add(op4);

        return  options;
    }

    public List<Option> fillOpt(String opt1, String opt2, String opt3, String opt4){
        List<Option> options = new ArrayList<>();
        options.add(new Option(opt1));
        options.add(new Option(opt2));
        options.add(new Option(opt3));
        options.add(new Option(opt4));

        return options;
    }

    public ObservableList<Question> loadListViewData() {
        List<Question> questions = new ArrayList<>();
        List<Option> options = new ArrayList<>();
        try {
            File file = new File("question.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(";");
                String nameQuestion = fields[0];
                options = fillOpt(fields);
                String correctAnswer = fields[5];
                Question q1 = new Question(nameQuestion, options, correctAnswer);
                questions.add(q1);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not open the 'question.txt' file.");
        }

        questionsObservableList = FXCollections.observableArrayList(questions);
        questionList.setItems(questionsObservableList);
        return questionsObservableList;
    }

    public void fillTextFieldsWithCurrentQuestion(){
        Question q = questionSelected();
        question.setText(q.getName());
        opt1.setText(q.getOptions().get(0).toString());
        opt2.setText(q.getOptions().get(1).toString());
        opt3.setText(q.getOptions().get(2).toString());
        opt4.setText(q.getOptions().get(3).toString());
    }

    public void fillTextFieldsWithCurrentQuestion(Question q){
        question.setText(q.getName());
        opt1.setText(q.getOptions().get(0).toString());
        opt2.setText(q.getOptions().get(1).toString());
        opt3.setText(q.getOptions().get(2).toString());
        opt4.setText(q.getOptions().get(3).toString());
    }

    public void fillTextFields(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            fillTextFieldsWithCurrentQuestion();
        }
    }

    public boolean validateAnswer(Question q, String bt_Check){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if(q.getCorrectAnswer().equals(bt_Check)){
            fillTextFieldsWithCurrentQuestion(nextQuestion(q));
            stillPlaying = true;
            score+=100;
            lb_Score.setText(String.valueOf(score));
        }
        else {
            alert.setTitle("Incorrect");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect Option, Try again");
            stillPlaying = false;
            alert.showAndWait();
        }
        return stillPlaying;
    }

    public int tryParseInt(String value) {
        int valueReturn;
        try {
            valueReturn = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            valueReturn = 0;
        }
        return valueReturn;
    }

    public Question newQuestion() {
        Question questionReturn;
        String name = question1.getText();
        List<Option> optionList = fillOpt(opt11.getText(), opt21.getText(), opt31.getText(), opt41.getText());
        String correctAnswerString = correctAnswer.getText();

        if (name.isEmpty() || correctAnswerString.isEmpty() || tryParseInt(correctAnswerString) > 4) {
            questionReturn =  null;
        }
        else questionReturn = new Question(name, optionList, correctAnswerString);

        return questionReturn;
    }

    public void addQuestionToFile() {
        String file = "GeneralQuestions.txt";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        Question newQuestion = newQuestion();
        if (newQuestion == null) {
            alert.setTitle("Missing Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all the required fields and with the correct format.");
            alert.showAndWait();
        }
        else {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                String line = newQuestion.formatLineForFile();
                writer.write(line);
                writer.newLine();
                alert.setTitle("Question successfully added!");
                alert.setHeaderText(null);
                alert.setContentText("In order not to modify the score, the new question will appear when " +
                        "restarting the app.");
                alert.showAndWait();
                System.out.println("Successful!!");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    public void clearTextFields(){
        question1.clear();
        opt11.clear();
        opt21.clear();
        opt31.clear();
        opt41.clear();
        correctAnswer.clear();
    }

    public void onAddQuestionToFileClicked(ActionEvent actionEvent) {
        addQuestionToFile();
        clearTextFields();
    }

    public Question questionSelected(){
        int selectedIndex = questionList.getSelectionModel().getSelectedIndex();
        return (Question) questionList.getItems().get(selectedIndex);
    }

    public Question nextQuestion(Question question){
        Question nextQuestion = null;
        boolean exit = false;
        for (int i = 0; i < questionsObservableList.size() && !exit; i++) {
            if(questionsObservableList.get(i).equals(question)){
                exit = true;
                nextQuestion = questionsObservableList.get(i+1);
            }
        }
        return nextQuestion;
    }

    public void checkAnswer(String bt_Check){
        int selectedIndex = questionList.getSelectionModel().getSelectedIndex();
        Question q = questionSelected();
        if (!stillPlaying) {
            validateAnswer(q, bt_Check);
        } else {
            questionList.getSelectionModel().selectNext();
            selectedIndex = questionList.getSelectionModel().getSelectedIndex();
            System.out.println(selectedIndex);
            Question q2 = questionsObservableList.get(selectedIndex);
            fillTextFieldsWithCurrentQuestion(q2);
            validateAnswer(q2, bt_Check);
        }
    }

    public void onCheckedClicked1(ActionEvent actionEvent) {
        checkAnswer("1");
    }

    public void onCheckedClicked4(ActionEvent actionEvent) {
        checkAnswer("4");
    }

    public void onCheckedClicked2(ActionEvent actionEvent) {
        checkAnswer("2");
    }

    public void onCheckedClicked3(ActionEvent actionEvent) {
        checkAnswer("3");
    }
}