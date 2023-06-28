package fx.javafx;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.jar.Attributes;

public class Question {
    private String name;
    private List<Option> options;
    private String correctAnswer;

    public Question(String name, List<Option> options, String correctAnswer) {
        this.name = name;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getName() {
        return name;
    }

    public List<Option> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String formatOptions(){
        String optionsString = "";
        for(Option op : options){
            optionsString += op.toString() + ";";
        }
        return optionsString;
    }

    public String formatLineForFile(){
        return name + ";" + formatOptions()  + correctAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(name, question.name) && Objects.equals(options, question.options) && Objects.equals(correctAnswer, question.correctAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, options, correctAnswer);
    }

    @Override
    public String toString() {
        return name;
    }
}