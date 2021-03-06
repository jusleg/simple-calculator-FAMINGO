package com.simplemobiletools.calculator.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.simplemobiletools.calculator.BuildConfig
import com.simplemobiletools.calculator.R
import com.simpletools.calculator.commons.extensions.config
import com.simpletools.calculator.commons.extensions.updateViewColors
import com.simplemobiletools.commons.helpers.LICENSE_AUTOFITTEXTVIEW
import com.simplemobiletools.commons.helpers.LICENSE_ESPRESSO
import com.simplemobiletools.commons.helpers.LICENSE_KOTLIN
import com.simplemobiletools.commons.helpers.LICENSE_ROBOLECTRIC
import me.grantland.widget.AutofitHelper

/* ktlint-disable no-wildcard-imports */
import com.simpletools.calculator.commons.helpers.*
import kotlinx.android.synthetic.main.activity_main.*
import com.simplemobiletools.commons.extensions.*
/* ktlint-enable no-wildcard-imports */

class MainActivity : BaseActivity() {

    lateinit var calc: CalculatorImpl

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calc = CalculatorImpl(this)

        btn_plus.setOnClickListener { calc.handleOperation(PLUS); checkHaptic(it) }
        btn_minus.setOnClickListener { calc.handleOperation(MINUS); checkHaptic(it) }
        btn_multiply.setOnClickListener { calc.handleOperation(MULTIPLY); checkHaptic(it) }
        btn_divide.setOnClickListener { calc.handleOperation(DIVIDE); checkHaptic(it) }
        btn_percentage.setOnClickListener { calc.handleOperation(PERCENTAGE); checkHaptic(it) }
        btn_negative.setOnClickListener { calc.handleOperation(NEGATIVE); checkHaptic(it) }
        btn_root.setOnClickListener { calc.handleOperation(ROOT); checkHaptic(it) }
        btn_power.setOnClickListener { calc.handleOperation(POWER); checkHaptic(it) }
        btn_squared.setOnClickListener { calc.handleOperation(SQUARED); checkHaptic(it) }
        btn_cubed.setOnClickListener { calc.handleOperation(CUBED); checkHaptic(it) }
        btn_mod.setOnClickListener { calc.handleOperation(MODULO); checkHaptic(it) }

        btn_clear.setOnClickListener { calc.handleClear(); checkHaptic(it) }
        btn_clear.setOnLongClickListener { calc.handleReset(); true }

        getButtonIds().forEach {
            it.setOnClickListener { calc.numpadClicked(it.id); checkHaptic(it) }
        }

        btn_equals.setOnClickListener { calc.handleEquals(); checkHaptic(it) }
        formula.setOnLongClickListener { copyToClipboard(false) }
        result.setOnLongClickListener { copyToClipboard(true) }

        AutofitHelper.create(result)
        AutofitHelper.create(formula)
        storeStateVariables()
        updateViewColors(calculator_holder, config.textColor)
    }

    @SuppressLint("MissingSuperCall")
    override fun onResume() {
        super.onResume()
        if (storedUseEnglish != config.useEnglish) {
            restartActivity()
            return
        }

        if (storedTextColor != config.textColor) {
            updateViewColors(calculator_holder, config.textColor)
        }
        vibrateOnButtonPress = config.vibrateOnButtonPress
    }

    override fun onPause() {
        super.onPause()
        storeStateVariables()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> launchSettings()
            R.id.about -> launchAbout()
            R.id.money -> launchMoney()
            R.id.draw -> launchDraw()
            R.id.base -> launchBase()
            R.id.crypto -> launchCrypto()
            R.id.unityConversion -> launchUnitConversion()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun storeStateVariables() {
        config.apply {
            storedTextColor = textColor
            storedUseEnglish = useEnglish
        }
    }

    private fun launchSettings() {
        startActivity(Intent(applicationContext, SettingsActivity::class.java))
    }

    private fun launchMoney() {
        startActivity(Intent(applicationContext, MoneyActivity::class.java))
    }

    private fun launchUnitConversion() {
        startActivity(Intent(applicationContext, UnitConversionActivity::class.java))
    }

    private fun launchDraw() {
        startActivity(Intent(applicationContext, DrawActivity::class.java))
    }

    private fun launchBase() {
        startActivity(Intent(applicationContext, BitwiseActivity::class.java))
    }

    private fun launchCrypto() {
        startActivity(Intent(applicationContext, CryptoActivity::class.java))
    }

    private fun launchAbout() {
        startAboutActivity(R.string.app_name, LICENSE_KOTLIN or LICENSE_AUTOFITTEXTVIEW or LICENSE_ROBOLECTRIC or LICENSE_ESPRESSO, BuildConfig.VERSION_NAME)
    }

    private fun getButtonIds() = arrayOf(btn_decimal, btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9)

    private fun copyToClipboard(copyResult: Boolean): Boolean {
        var value = formula.value
        if (copyResult) {
            value = result.value
        }

        return if (value.isEmpty()) {
            false
        } else {
            copyToClipboard(value)
            true
        }
    }

    override fun setClear(text: String) {
        btn_clear.text = text
    }

    override fun getFormula(): String {
        return formula.text.toString()
    }

    override fun setFormula(value: String) {
        formula.text = value
    }
}
