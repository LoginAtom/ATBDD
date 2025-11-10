package ru.netology.web.data.test;


import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;

import ru.netology.web.data.page.DashBoardPage;
import ru.netology.web.data.page.LoginPageV1;

import static org.junit.jupiter.api.Assertions.assertAll;
import static ru.netology.web.data.DataHelper.invalidBalance;
import static ru.netology.web.data.DataHelper.validBalance;


public class MoneyTransferTest {

    DashBoardPage dashBoardPage;
    DataHelper.CardInfo firstCardInfo;
    DataHelper.CardInfo secondCardinfo;
    int firstCardBalance;
    int secondCardBalance;

    @BeforeEach
    void setup() {
        var loginPage = Selenide.open("http://localhost:9999", LoginPageV1.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var vverificationCode = DataHelper.getVerificationCodeFor();
        dashBoardPage = verificationPage.validVerify(vverificationCode);
        firstCardInfo = DataHelper.getFirstCardInfo();
        secondCardinfo = DataHelper.getSecondtCardInfo();
        firstCardBalance = dashBoardPage.getCardBalance(firstCardInfo);
        secondCardBalance = dashBoardPage.getCardBalance(secondCardinfo);
    }


    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        var amount = validBalance(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        var transferPage = dashBoardPage.selectCard(secondCardinfo);
        dashBoardPage = transferPage.completeValidTransfer(String.valueOf(amount), firstCardInfo);
        dashBoardPage.reloadDashboardPage();
        assertAll(
                () -> dashBoardPage.checkCardBalance(firstCardInfo, expectedBalanceFirstCard),
                () -> dashBoardPage.checkCardBalance(secondCardinfo, expectedBalanceSecondCard)

        );

    }

    @Test
    void shouldErrorTransferMoneyBetweenOwnCards() {
        var amount = invalidBalance(secondCardBalance);
        var transferPage = dashBoardPage.selectCard(firstCardInfo);

        transferPage.completeTransfer(String.valueOf(amount), secondCardinfo);

        assertAll(
                () -> transferPage.searchError(""),
                () -> dashBoardPage.reloadDashboardPage(),
                () -> dashBoardPage.checkCardBalance(firstCardInfo, firstCardBalance),
                () -> dashBoardPage.checkCardBalance(secondCardinfo, secondCardBalance)

        );

    }

}


