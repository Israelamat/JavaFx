module fx.javafx.questionsfinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.testng;
    requires junit;


    opens fx.javafx to javafx.fxml;
    exports fx.javafx;
}