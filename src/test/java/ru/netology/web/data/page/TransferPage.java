package ru.netology.web.data.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;


import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    //
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private final SelenideElement amountInput = $("[data-test-id='amount'] input");
    private final SelenideElement fromInput = $("[data-test-id='from'] input");
    private final SelenideElement transferHead = $(byText("Пополнение карты"));
    private final SelenideElement errorMessage = $("[data-test-id='error-notification'] .notification__content");

    public TransferPage() {
        transferHead.shouldBe(visible);
    }

    // успешный перевод
    public DashBoardPage completeValidTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo) {
        completeTransfer(amountToTransfer, cardInfo);
        return new DashBoardPage();
    }

    // метод заполнение формы
    public void completeTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo) {
        amountInput.setValue(amountToTransfer);
        fromInput.setValue(cardInfo.getNumber());
        transferButton.click();
    }

    //метод проверки ошибок
    public void searchError(String expectedText) {
        errorMessage.should(Condition.visible, Duration.ofSeconds(20)).should(Condition.text(expectedText));

    }


}