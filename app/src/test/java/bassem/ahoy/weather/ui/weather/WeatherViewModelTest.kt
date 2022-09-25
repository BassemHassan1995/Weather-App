package bassem.ahoy.weather.ui.weather

import bassem.ahoy.weather.data.model.Coord
import bassem.ahoy.weather.data.model.WeekForecast
import bassem.ahoy.weather.data.notifications.NotificationScheduler
import bassem.ahoy.weather.data.repository.Repository
import bassem.ahoy.weather.utils.CoroutineTestRule
import bassem.ahoy.weather.utils.DataResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

private val VALID_WEEK_FORECAST =
    WeekForecast(city = "Cairo", cityId = 360630, country = "EG", weatherDays = emptyList())
private val THROWABLE = Throwable("Error")

private val VALID_COORD: Coord = spy(Coord(30.0626, 31.2497))
private val INVALID_COORD: Coord = spy(Coord(0.0, 0.0))

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var repository: Repository
    private lateinit var notificationScheduler: NotificationScheduler


    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        repository = mock()
        notificationScheduler = mock()
        viewModel = spy(WeatherViewModel(repository, notificationScheduler))
    }

    @Test
    fun testGetDataByLocation_validLocation_returnData() =
        testGetDataByLocation(VALID_COORD, DataResult.Success(VALID_WEEK_FORECAST))

    @Test
    fun testGetDataByLocation_invalidLocation_returnError() =
        testGetDataByLocation(INVALID_COORD, DataResult.Failure(THROWABLE))

    private fun testGetDataByLocation(coord: Coord, dataResult: DataResult<WeekForecast>) =
        runTest {
            whenever(repository.getWeekForecast(latitude = any(), longitude = any()))
                .doReturn(dataResult)
            with(viewModel)
            {
                getData(coord)
                verify(this).handleResult(dataResult)
                when (dataResult) {
                    is DataResult.Failure -> verify(this).sendEvent(any())
                    is DataResult.Success -> {
                        this.currentForecast.value?.let {
                            assert(it.weatherDays.size == VALID_WEEK_FORECAST.weatherDays.size)
                            assert(it.city == VALID_WEEK_FORECAST.city)
                        }
                    }
                }
            }
        }
}