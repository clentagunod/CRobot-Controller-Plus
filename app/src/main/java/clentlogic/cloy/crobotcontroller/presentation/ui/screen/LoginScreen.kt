package clentlogic.cloy.crobotcontroller.presentation.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import clentlogic.cloy.crobotcontroller.R
import clentlogic.cloy.crobotcontroller.presentation.contracts.MainViewContract
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.DeepTeal
import clentlogic.cloy.crobotcontroller.presentation.ui.theme.LimeGreen
import clentlogic.cloy.crobotcontroller.presentation.ui.util.getScreenSize

@Composable
fun LoginScreen(
    viewModel: MainViewContract,
    onLocal: () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenSize = getScreenSize()

    val interactionSource = remember { MutableInteractionSource() }
    var isPasswordVisible by remember { mutableStateOf(false) }


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Box(
        modifier = modifier
            .background(DeepTeal)
            .fillMaxSize()
    ) {
        Image(
            painterResource(R.drawable.background),
            contentDescription = null,
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .height(screenSize.h * 0.30f)
                .offset(y = screenSize.h * 0.15f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painterResource(R.drawable.login),
                    contentDescription = null,
                )
                Spacer(Modifier.height(30.dp))

                Text(
                    "LOGIN",
                    color = Color.White

                )

            }

        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .height(screenSize.h * 0.50f)
                .offset(y = screenSize.h * 0.30f)
        ) {
            Column() {
                TextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    label = { Text(
                        "Email",
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelSmall
                    ) },
                    singleLine = true,
                    placeholder = {
                        Text(
                            "Enter your email",
                            style = MaterialTheme.typography.labelSmall
                            )
                    },
                    modifier = modifier.clip(RoundedCornerShape(8.dp))
                )

                Spacer(Modifier.height(20.dp))

                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    label = {
                        Text(
                            "Password",
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.labelSmall
                        )
                            },
                    singleLine = true,
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (isPasswordVisible) R.drawable.visiblepassword else R.drawable.notvisiblepassword

                        IconButton(
                            onClick = {
                                isPasswordVisible = !isPasswordVisible
                            }
                        ) {
                            Icon(
                                painterResource(icon),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                    },
                    placeholder = {
                        Text(
                            "Enter your password",
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    modifier = modifier.clip(RoundedCornerShape(8.dp))
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .height(screenSize.h * 0.30f)
                .offset(y = screenSize.h * 0.65f)

        ){

            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Button(
                    //TODO: Must improve this USER component
                    onClick = {
                        viewModel.setLoginCredential("clent", email, password)
                    },
                    interactionSource = interactionSource,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LimeGreen,
                        contentColor = DeepTeal,
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                ) {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                TextButton(
                    onClick = {
                        onLocal()
                    }
                ) {
                    Text(
                        "Use local only",
                        textDecoration = TextDecoration.Underline,
                        fontSize = 14.sp,
                        color = Color.White.copy(0.70f),
                        style = MaterialTheme.typography.labelSmall
                    )
                }

            }


        }



    }


}