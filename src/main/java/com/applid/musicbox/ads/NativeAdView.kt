import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdOptions.ADCHOICES_TOP_RIGHT

//TODO SHOULD BE FIXED
@Composable
fun NativeAdView(adUnitId: String, context: Context) {
    //TODO try this -> AndroidViewBinding(factory = LayoutNativeAdBinding::inflate) {
    var adViewState by remember { mutableStateOf<AdViewState>(AdViewState.Loading) }
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    val adLoader = remember(adUnitId) {
        AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad: NativeAd ->
                // Ad loaded successfully, extract components and display in UI
                val adHeadline: String? = ad.headline
                val adImage: NativeAd.Image? = ad.icon
                val adCallToAction: String? = ad.callToAction
                val adAdvertiser: String? = ad.advertiser


                // Update state to trigger recomposition and display the ad components in your UI
                adViewState = AdViewState.Success(adHeadline, adImage, adCallToAction, adAdvertiser)
                nativeAd = ad
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    // Update state to trigger recomposition and handle ad failed to load event
                    adViewState = AdViewState.Error(p0)
                }
            })

            .withNativeAdOptions(NativeAdOptions.Builder()
                .setAdChoicesPlacement(ADCHOICES_TOP_RIGHT)
                .build())

            .build()
    }

    LaunchedEffect(adUnitId) {
        adLoader.loadAd(AdRequest.Builder().build())
    }

    // Check if the NativeAd instance should be destroyed
    DisposableEffect(nativeAd) {
        onDispose {
            nativeAd?.destroy()
        }
    }
    
    when (adViewState) {
        is AdViewState.Loading -> {
            // Display loading indicator while the ad is being loaded
            // For example, you can use a CircularProgressIndicator here
        }
        is AdViewState.Success -> {
            // Display the ad components in your UI
            // For example, you can use a Column with an Image and a Text component here
            val adHeadline = (adViewState as AdViewState.Success).headline
            val adIcon = (adViewState as AdViewState.Success).icon
            val adCallToAction = (adViewState as AdViewState.Success).callToAction
            val adAdvertiser = (adViewState as AdViewState.Success).advertiser



            Box(modifier = Modifier.padding(12.dp, 12.dp, 4.dp, 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (adIcon != null) {
                        Image(
                            painter = rememberAsyncImagePainter(adIcon.uri),
                            contentDescription = adHeadline,
                            modifier = Modifier
                                .size(45.dp)
                                .clip(RoundedCornerShape(10.dp)),


                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = adHeadline ?: "",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = LocalTextStyle.current.color,
                            ),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Row {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(4.dp)
                                ).padding(vertical = 1.dp, horizontal = 3.dp)
                            ) {
                                Text(
                                    text = "Ad",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),

                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = adAdvertiser ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,


                                    )


                        }

                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        text = adCallToAction ?: "",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .clickable(onClick = {
                                // Handle ad call to action button click
                            })

                    )

                }
            }
        }
        is AdViewState.Error -> {
            // Display error message if the ad failed to load
            // For example, you can use a Text component here
            val errorMessage = (adViewState as AdViewState.Error).error.message
            Log.e("NativeAdError", errorMessage)
        }
        else -> {
            // Handle unexpected case
            Log.e("NativeAdError","Unexpected state")
        }
    }
}

sealed class AdViewState {
    object Loading : AdViewState()
    data class Success(
        val headline: String?,
        val icon: NativeAd.Image?,
        val callToAction: String?,
        val advertiser: String?
    ) : AdViewState()
    data class Error(val error: LoadAdError) : AdViewState()
}
