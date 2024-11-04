* There's no Icons.Filled.Minus sign.

See https://developer.android.com/reference/kotlin/androidx/compose/material/icons/package-summary
Add this to the app dependencies:
implementation("androidx.compose.material:material-icons-extended")

I copied the definition from Icons.Default.Remove, and then I removed the dependency.
