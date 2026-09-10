package com.gios.brightthumb.ui.components.settings.about

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.NewReleases
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.gios.brightthumb.CrashLog
import com.gios.brightthumb.R
import com.gios.brightthumb.utils.SimpleTopAppBar
import com.gios.brightthumb.utils.TAG
import com.gios.brightthumb.utils.openLink
import me.zhanghai.compose.preference.Preference
import me.zhanghai.compose.preference.PreferenceCategory
import me.zhanghai.compose.preference.ProvidePreferenceTheme

const val GITHUB_URL = "https://github.com/gi-os/BrightThumb"
const val USER_GUIDE_URL = "https://github.com/dessalines/thumb-key#user-guide"
const val UPSTREAM_URL = "https://github.com/dessalines/thumb-key"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    Log.d(TAG, "Got to About activity")

    val ctx = LocalContext.current

    val version = ctx.packageManager.getPackageInfo(ctx.packageName, 0).versionName

    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    // Read once, on entry. The log only changes when the process dies, and by then
    // this screen is gone with it.
    val crashLog = remember { CrashLog.read(ctx) }
    var crashDialogOpen by remember { mutableStateOf(false) }

    if (crashDialogOpen && crashLog != null) {
        AlertDialog(
            onDismissRequest = { crashDialogOpen = false },
            title = { Text(stringResource(R.string.last_crash)) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        text = crashLog,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    CrashLog.copyToClipboard(ctx, crashLog)
                    crashDialogOpen = false
                }) {
                    Text(stringResource(R.string.copy_crash_log))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    CrashLog.clear(ctx)
                    crashDialogOpen = false
                }) {
                    Text(stringResource(R.string.clear_crash_log))
                }
            },
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SimpleTopAppBar(text = stringResource(R.string.about), navController = navController)
        },
        content = { padding ->
            Column(
                modifier =
                    Modifier
                        .padding(padding)
                        .verticalScroll(scrollState)
                        .background(color = MaterialTheme.colorScheme.surface),
            ) {
                ProvidePreferenceTheme {
                    Preference(
                        title = { Text(stringResource(R.string.whats_new)) },
                        summary = { Text(stringResource(R.string.version, version.orEmpty())) },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.NewReleases,
                                contentDescription = stringResource(R.string.releases),
                            )
                        },
                        onClick = {
                            openLink("$GITHUB_URL/releases", ctx)
                        },
                    )
                    SettingsDivider()
                    PreferenceCategory(
                        title = { Text(stringResource(R.string.support)) },
                    )
                    Preference(
                        title = { Text(stringResource(R.string.issue_tracker)) },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.BugReport,
                                contentDescription = stringResource(R.string.issue_tracker),
                            )
                        },
                        onClick = {
                            openLink("$GITHUB_URL/issues", ctx)
                        },
                    )
                    // Only shown when there is something to show. A keyboard dies in the
                    // background, where nobody is watching, so the stack trace has to
                    // outlive the process and be readable without a cable.
                    if (crashLog != null) {
                        Preference(
                            title = { Text(stringResource(R.string.last_crash)) },
                            summary = {
                                Text(
                                    text = crashLog.lineSequence().firstOrNull().orEmpty(),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.WarningAmber,
                                    contentDescription = stringResource(R.string.last_crash),
                                )
                            },
                            onClick = { crashDialogOpen = true },
                        )
                    }
                    SettingsDivider()
                    PreferenceCategory(
                        title = { Text(stringResource(R.string.open_source)) },
                    )
                    Preference(
                        title = { Text(stringResource(R.string.source_code)) },
                        summary = {
                            Text(stringResource(R.string.source_code_subtitle))
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Code,
                                contentDescription = stringResource(R.string.source_code),
                            )
                        },
                        onClick = {
                            openLink(GITHUB_URL, ctx)
                        },
                    )
                    Preference(
                        title = { Text(stringResource(R.string.upstream_project)) },
                        summary = { Text(stringResource(R.string.upstream_project_subtitle)) },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Code,
                                contentDescription = stringResource(R.string.upstream_project),
                            )
                        },
                        onClick = {
                            openLink(UPSTREAM_URL, ctx)
                        },
                    )
                }
            }
        },
    )
}

@Composable
fun SettingsDivider() {
    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
}

@Preview
@Composable
fun AboutPreview() {
    AboutScreen(navController = rememberNavController())
}
