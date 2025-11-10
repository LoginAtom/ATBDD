package ru.netology.web.data.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashBoardPage {

    // к сожалению, разработчики не дали нам удобного селектора, поэтому так
    private final ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private final SelenideElement heading = $("[data-test-id=dashboard]");
    private final SelenideElement reloadButton = $("[data-test-id='action-reload']");

    public DashBoardPage() {

        heading.shouldBe(visible);
    }

    // найти карту
    private SelenideElement getCard(DataHelper.CardInfo cardInfo) {
        return cards.find(Condition.attribute("data-test-id", cardInfo.getTestId()));
    }

    //метод для получения баланса любой карты
    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        var text = getCard(cardInfo).getText();
        return extractBalance(text);
    }

    //метод выбрать карту для пополнения
    public TransferPage selectCard(DataHelper.CardInfo cardInfo) {
        getCard(cardInfo).$("button").click();
        return new TransferPage();
    }

    //метод перезагрузки страницы
    public void reloadDashboardPage() {
        reloadButton.click();
        heading.shouldBe(visible);
    }


    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    //показывает баланс выбранной карты
    public void checkCardBalance(DataHelper.CardInfo cardInfo, int expectedBalance) {
        getCard(cardInfo).should(visible).should(text(balanceStart + expectedBalance + balanceFinish));
    }
}
