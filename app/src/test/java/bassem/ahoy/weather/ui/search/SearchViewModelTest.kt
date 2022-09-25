package bassem.ahoy.weather.ui.search

import bassem.ahoy.weather.data.model.CityResponse
import bassem.ahoy.weather.data.repository.Repository
import bassem.ahoy.weather.utils.CoroutineTestRule
import bassem.ahoy.weather.utils.DataResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

private const val VALID_CITY = "Cairo"
private const val INVALID_CITY = "Anything"

private val THROWABLE = Throwable("Error")
private val VALID_CITY_LIST = listOf(
    CityResponse(name = "Cairo", country = "EG", lat = 30.0626, lon = 31.2497),
    CityResponse(name = "Cairo", country = "US", lat = 37.0057958, lon = -89.1772449),
)

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private lateinit var repository: Repository


    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        repository = mock()
        viewModel = spy(SearchViewModel(repository))
    }

    @Test
    fun testGetDataBySearch_validQuery_returnData() =
        testGetDataBySearch(VALID_CITY, DataResult.Success(VALID_CITY_LIST))

    @Test
    fun testGetDataBySearch_invalidQuery_returnError() =
        testGetDataBySearch(
            INVALID_CITY,
            DataResult.Failure(THROWABLE)
        )


    private fun testGetDataBySearch(query: String, dataResult: DataResult<List<CityResponse>>) =
        runTest {
            whenever(repository.searchCity(query))
                .doReturn(dataResult)
            with(viewModel)
            {
                searchForCity(query)
                when (dataResult) {
                    is DataResult.Failure -> verify(this).sendEvent(any())
                    is DataResult.Success -> {
                        assert(this.cities.value.size == VALID_CITY_LIST.size)
                    }
                }
            }

        }

}