package com.russartstudio.budget

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private val db: FirebaseFirestore= FirebaseFirestore.getInstance()
    private val c: Calendar = Calendar.getInstance()
    private val strYear=c.get(Calendar.YEAR).toString()
    private val strDate="${c.get(Calendar.MONTH)+1}.${strYear.takeLast(2)}"    //Date format
    private val budgetRef = db.collection("Months").document(strDate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvBudget.text+= strDate
        incomeListener()
        debtListener()
        reservesListener()
        budgetListener()
        expensesListener()
        startActivity()

    }

    override fun onStart() {
        super.onStart()
        budgetRef.addSnapshotListener{ snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                tvBudgetNb.text= noDisplayNull(snapshot.getDouble("Budget"))
                tvReservesNb.text= noDisplayNull(snapshot.getDouble("Reserves"))
                tvIncomeNb.text= noDisplayNull(snapshot.getDouble("Income"))
                tvDebtNb.text= noDisplayNull(snapshot.getDouble("Debt"))
                tvExpensesNb.text=noDisplayNull(snapshot.getDouble("Expenses"))
                val budget=snapshot.getDouble("Budget")
                val expenses=snapshot.getDouble("ExpensesSum")
                if( budget!=null && expenses!=null)
                tvBalanceNb.text= (budget-expenses).toString()
                tvPleasures.text= (getString(R.string.pleasures)+noDisplayNull(snapshot.getDouble("Pleasures")))
                tvSocialize.text=(getString(R.string.socialize)+noDisplayNull(snapshot.getDouble("Socialize")))
                tvSupply.text=(getString(R.string.supply)+noDisplayNull(snapshot.getDouble("Supply")))
                tvOthers.text=(getString(R.string.others)+noDisplayNull(snapshot.getDouble("Others")))
                tvExpensesNb.text=noDisplayNull(snapshot.getDouble("ExpensesSum"))
            }
            else{
                val c: Calendar = Calendar.getInstance()   //previous date
                c.add(Calendar.MONTH, -1)
                val strYear=c.get(Calendar.YEAR).toString()
                val strDate="${c.get(Calendar.MONTH)+1}.${strYear.takeLast(2)}"
                val prevdocRef=db.collection("Months").document(strDate)
                prevdocRef.addSnapshotListener { value, error ->
                    if(error!=null){
                        return@addSnapshotListener
                    }
                    if (value != null && value.exists()){
                        val reserves= snapshot?.getDouble("Reserves") ?: 0.0
                        val balance=snapshot?.getDouble("Balance") ?: 0.0
                        val cv= hashMapOf(
                            "Reserves" to FieldValue.increment(reserves),
                            "Balance" to FieldValue.increment(balance)
                        )
                        budgetRef.set(cv)

                    }
                }

            }

        }
    }

    private fun incomeListener(){
        val dialog = MainDialog(
            R.string.income,
            R.style.IncomeTheme,
            getString(R.string.income),
            "Income",
            strDate
        )
        tvIncome.setOnClickListener {
            dialog.show(supportFragmentManager, "Dialog")

        }
        tvIncomeNb.setOnClickListener {
            dialog.show(supportFragmentManager, "Dialog")

        }
    }
    private fun debtListener(){
        val dialog = SecondaryDialog(R.string.debt, R.style.DebtTheme, strDate,this)
        tvDebt.setOnClickListener {
            dialog.show(supportFragmentManager, "Dialog")
        }
        tvDebtNb.setOnClickListener {
            dialog.show(supportFragmentManager, "Dialog")
        }

    }
    private fun reservesListener(){
        val dialog = MainDialog(
            R.string.reserves,
            R.style.ReservesTheme,
            getString(R.string.reserves),
            "Reserves",
            strDate
        )
        tvReserves.setOnClickListener {
            dialog.show(supportFragmentManager, "Dialog")
        }
        tvReservesNb.setOnClickListener {
            dialog.show(supportFragmentManager, "Dialog")
        }

    }
    private fun budgetListener(){
        val dialog = MainDialog(R.string.budget, R.style.BudgetTheme, "Add some funds", "Budget", strDate)
        tvBudget.setOnClickListener {
            dialog.show(supportFragmentManager, "Dialog")
        }
        tvBudgetNb.setOnClickListener {
            dialog.show(supportFragmentManager, "Dialog")
        }

    }
    private fun expensesListener(){
        tvPleasures.setOnClickListener {
            val dialog = MainDialog(
                R.string.expenses,
                R.style.ExpensesTheme,
                getString(R.string.pleasures),
                "Pleasures",
                strDate
            )
            dialog.show(supportFragmentManager, "Dialog")
        }
        tvSocialize.setOnClickListener {
            val dialog = MainDialog(
                R.string.expenses,
                R.style.ExpensesTheme,
                getString(R.string.socialize),
                "Socialize",
                strDate
            )
            dialog.show(supportFragmentManager, "Dialog")
        }
        tvSupply.setOnClickListener {
            val dialog = MainDialog(
                R.string.expenses,
                R.style.ExpensesTheme,
                getString(R.string.supply),
                "Supply",
                strDate
            )
            dialog.show(supportFragmentManager, "Dialog")
        }
        tvOthers.setOnClickListener {
            val dialog = MainDialog(
                R.string.expenses,
                R.style.ExpensesTheme,
                getString(R.string.others),
                "Others",
                strDate
            )
            dialog.show(supportFragmentManager, "Dialog")
        }


    }
    private fun startActivity(){
        ivLoan.setOnClickListener {
         val intent = Intent(this, DebtActivity::class.java)
         startActivity(intent)
         }
    }
    private fun noDisplayNull(num: Double?):String{
        return num?.toString() ?: "0.0"

    }
}