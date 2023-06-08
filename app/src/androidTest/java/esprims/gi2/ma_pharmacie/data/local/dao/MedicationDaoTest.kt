package esprims.gi2.ma_pharmacie.data.local.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MedicationDaoTest {

   /* private  lateinit var myFakeDatabase: AppDatabase
    private  lateinit var medicineDao: MedicineDao

    @Before
    fun setup()
    {

        myFakeDatabase= Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        medicineDao=myFakeDatabase.medicineDao()
    }
       // it's a test thar belong to RED ,GREEN and refactor ,
       // to be sure that test works correctly and didn't always
    // return true
    @Test
    fun whenICheckIfNonInsertedMedicineIsInListOfMedicineItShouldFail()
    {
        // arrange
        val medicine=Medicine(name = "medicine", codabar = "5555225", ageCategory = "child"
            , gender = "Male",form = "pilule", expirationDate = 55555555555,
            additionalDescription = "after eating", type = "paracetamol"
        )

        // assert
        val allMedicines=medicineDao.getAll()

        assert(medicine in allMedicines)
    }

    //check if autogeneration of id works correctly
    @Test
    fun whenIInsertInsertedMedicineIthShouldFail()
    {
        // arrange
        val medicine=Medicine(name = "medicine", codabar = "5555225",
            ageCategory = "child", gender = "Male",form = "pilule"
            , expirationDate = 55555555555, additionalDescription = "after eating",
            type = "paracetamol"
        )

        //act
        medicineDao.insert(medicine)

        // assert

        medicineDao.insert(medicine)

    }

    // when i insert a medicine , it's surely expected that this medicine will be
    // available when i get all the medicines
    @Test
    fun whenICheckIfInsertedMedicineIsInListOfMedicineItShouldPass()
    {
        // arrange
        val medicine=Medicine(name = "medicine", codabar = "5555225", ageCategory = "child"
            , gender = "Male",form = "pilule", expirationDate = 55555555555,
             additionalDescription = "after eating", type = "paracetamol"
        )
        //act
        medicineDao.insert(medicine)

        // assert
        val allMedicines=medicineDao.getAll()

        assert(medicine in allMedicines)
    }
*/

}