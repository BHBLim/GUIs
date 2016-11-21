/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wooppy.gui.beans

import groovy.beans.Bindable
import groovy.transform.Canonical

/**
 *
 * @author lisab
 */
@Canonical
@Bindable
class TestBean {
	int testInt
        String testString
        boolean testBool
}

