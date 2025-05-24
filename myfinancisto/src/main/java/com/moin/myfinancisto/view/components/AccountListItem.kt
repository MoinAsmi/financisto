/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */
package com.moin.myfinancisto.view.components

/**
 *
 */
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moin.financisto.data.entities.Account
import com.moin.financisto.data.entities.AccountType
import com.moin.myfinancisto.R
import com.moin.myfinancisto.ui.theme.TextColors
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@Composable
@Preview
fun AccountListItem(
    account: Account = Account(
        accountId = 1,
        userId = 1,
        name = "Cash in hand",
        balance = 70,
        isActive = true,
        accountType = AccountType.Cash,
        creditLimit = 600
    ),
    lastTransactionOn: Date = Date()
) {
    Row(
        modifier = Modifier
            .height(if (account.accountType == AccountType.CreditCard) 84.dp else 72.dp)
            .fillMaxWidth()
            .background(color = Color.Black)
            .padding(horizontal = 12.dp)
    ) {
        AccountIcon(account)
        Spacer(modifier = Modifier.width(5.dp))
        Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .width(1.dp)
                    .background(Color.Gray)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = account.accountType.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                color = TextColors.SecondaryText
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = account.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    color = TextColors.PrimaryText
                )
                val balance = computeBalanceToBeShown(account)
                Text(
                    text = "₹ $balance",
                    style = MaterialTheme.typography.bodyLarge,
                    color = when {
                        balance < 0 -> TextColors.NegativeAmount
                        balance > 0 -> TextColors.PositiveAmount
                        else -> TextColors.ZeroAmount
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = SimpleDateFormat.getInstance().format(lastTransactionOn),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    color = TextColors.SecondaryText
                )
                if (account.accountType == AccountType.CreditCard) {
                    Text(
                        text = "₹ " + account.balance.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 5.dp),
                        color = when {
                            account.balance < 0 -> TextColors.NegativeAmount
                            account.balance > 0 -> TextColors.PositiveAmount
                            else -> TextColors.ZeroAmount
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            if (account.accountType == AccountType.CreditCard) {
                val balanceCredit = account.creditLimit + account.balance
                GradientProgressBar(
                    modifier = Modifier.height(12.dp),
                    colors = listOf(Color(0xffef5350), Color(0xff66bb6a), Color(0xff66bb6a)),
                    progress = balanceCredit / account.creditLimit.toFloat()
                )
            }
        }
    }

}

@Composable
private fun AccountIcon(account: Account) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(53.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = account.accountType.iconResId),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 5.dp)
                .size(48.dp)
                .alpha(if (account.isActive) 1.0f else 0.5f),
//            colorFilter = ColorFilter.tint(Color(0x33999999))
        )
        if (account.isActive.not()) {
            Image(
                painter = painterResource(id = R.drawable.icon_lock),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
private fun computeBalanceToBeShown(account: Account): Long =
    if (account.accountType == AccountType.CreditCard) account.creditLimit + account.balance else account.balance

@Composable
fun GradientProgressBar(modifier: Modifier = Modifier, progress: Float, colors: List<Color>) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(colors = colors))
    ) {
        Box(modifier = modifier.fillMaxWidth(progress))
        Box(modifier = modifier
            .fillMaxSize()
            .background(color = Color.Gray))
    }
}
