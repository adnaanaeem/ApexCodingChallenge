package com.apex.codeassesment.ui.main.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.apex.codeassesment.R
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.ui.main.MainUiState
import com.apex.codeassesment.ui.main.MainViewEvent

@Composable
fun MainScreen(
    uiStateData: MainUiState,
    onEvent: (MainViewEvent) -> Unit,
) {
    var mutableUserList by remember { mutableStateOf(listOf<User>()) }
    mutableUserList = uiStateData.userList

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.my_random_user),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        UserInfoCard(uiStateData){ event ->  onEvent(event)}

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onEvent(MainViewEvent.RefreshEvent(true)) },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.refresh_random_user))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Divider(
            color = Color(0x11111111),
            thickness = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onEvent(MainViewEvent.UserListEvent) },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.show_10_users))
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            itemsIndexed(items = mutableUserList) { index, item ->
                UserListItem(item) {
                    onEvent(MainViewEvent.UserDetailsEvent(item))
                }
            }
        }
    }
}


@Composable
fun UserInfoCard(
    uiStateData: MainUiState,
    onEvent: (MainViewEvent) -> Unit,
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .height(100.dp)
                .background(Color(0x33aaaaaa))
        ) {
            Image(
                painter = rememberAsyncImagePainter(uiStateData.randomUser.picture?.medium),
                contentDescription = uiStateData.randomUser.name.toString(),
                modifier = Modifier.size(100.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = stringResource(id = R.string.name),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = uiStateData.randomUser.name?.first.toString(),
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = stringResource(id = R.string.email),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = uiStateData.randomUser.email.toString(),
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onEvent(MainViewEvent.UserDetailsEvent(uiStateData.randomUser)) },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.view_details))
            }
        }
    }
}

@Composable
fun UserListItem(
    user: User,
    onItemClick: () -> Unit
) {
    Text(
        text = user.name.toString(),
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier
            .clickable(enabled = true, onClick = onItemClick)
            .fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .background(color = Color.White)
    ) {
        // MainScreen()
    }
}
