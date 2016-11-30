/**
 *	Button Controller App for ZWN-SC7
 *
 *	Author: Philippe Gravel
 *	Date Created: 2016-03-04
 *
 */
definition(
    name: "Kitchen Control",
    namespace: "philippegravel",
    author: "Philippe Gravel",
    description: "Control of all the kitchen",
    category: "Convenience",
    iconUrl: "http://cdn.device-icons.smartthings.com/Food & Dining/dining5-icn@2x.png",
    iconX2Url: "http://cdn.device-icons.smartthings.com/Food & Dining/dining5-icn@2x.png"
)

preferences {
	page(name: "Controller")
    page(name: "Cuisine")
    page(name: "Other")
}

def Controller() {
  dynamicPage(name: "Controller", title: "First, select which ZWN-SC7", nextPage: "Cuisine", uninstall: true) {
    section ("Set the controller") {
      input "buttonDevice", "capability.button", title: "Controller", multiple: false, required: true
    }
    section(title: "Virtual Controller") {
    	input "virtualController", "capability.switch", title: "Virtual Controller", required: true
    }
    section(title: "Other", mobileOnly:true, hideable: true, hidden: true) {
		label title: "Assign a name", required: false
    }
    
	section("Send Notifications?") {
    	input("recipients", "contact", title: "Send notifications to", multiple: true, required: false)
    }
  }
}

def Cuisine() {
  dynamicPage(name: "Cuisine", title: "Cuisine setup", nextPage: "Other", uninstall: true) {
    section {
	  input "table", "capability.switch", title: "Table:", required: true
      input "comptoir", "capability.switch", title: "Comptoir:", required: true
      input "pont", "capability.switch", title: "Pont:", required: true
      input "strip", "capability.switch", title: "Dessous comptoir:", required: true
      input "plafond", "capability.switch", title: "Planfond:", required: true
      input "top", "capability.switch", title: "Planfond couleur:", required: true
      input "dresser", "capability.switch", title: "Vaisselier:", required: true
      input "portelock", "capability.lock", title: "Porte", required: true
      input "shades", "capability.windowShade", title: "Fenetres", required: true
    }
  }
}

def Other() {
  dynamicPage(name: "Other", title: "Other setup", install: true, uninstall: true) {
  	section {
      input "visitor", "capability.switch", title: "Visitor switch", required: true
      input "outside", "capability.switch", title: "Switch exterieur", required: true
    }
  }  
}  

def installed() {
  initialize()
}

def updated() {
  unsubscribe()
  initialize()
}

def initialize() {

  subscribe(buttonDevice, "button", buttonEvent)

    if (relayDevice) {
        log.debug "Kicthen Control: Associating ${relayDevice.deviceNetworkId}"
        if (relayAssociate == true) {
            buttonDevice.associateLoad(relayDevice.deviceNetworkId)
        }
        else {
            buttonDevice.associateLoad(0)
        }
    }

	subscribe(pont, "switch", pontEvent)
    subscribe(plafond, "switch", plafondEvent)
    subscribe(top, "switch", topEvent)
    
    subscribe(visitor, "switch", visitorEvent)
    
    subscribe(virtualController, "level", virtualLevelEvent)
    subscribe(virtualController, "switch", virtualOffEvent)
}

def buttonEvent(evt){
 	log.debug "Kicthen Control: buttonEvent"

	def buttonNumber = evt.jsonData.buttonNumber
    def firstEventId = 0
    def value = evt.value
    log.debug "buttonEvent: $evt.name = $evt.value ($evt.data)"
 //   log.debug "Kicthen Control: button: $buttonNumber, value: $value"
    def recentEvents = buttonDevice.eventsSince(new Date(now() - 3000)).findAll{it.value == evt.value && it.data == evt.data}
    log.debug "Kicthen Control: Found ${recentEvents.size()?:0} events in past 3 seconds"
    if (recentEvents.size() != 0){
        log.debug "Kicthen Control: First Event ID: ${recentEvents[0].id}"
        firstEventId = recentEvents[0].id
    }
    else {
        firstEventId = 0
    }

    log.debug "Kicthen Control: This Event ID: ${evt.id}"

    if(firstEventId == evt.id){
        switch(buttonNumber) {
            case ~/.*1.*/:
            button1Handlers()
            break
            case ~/.*2.*/:
            button2Handlers()
            break
            case ~/.*3.*/:
            button3Handlers()
            break
            case ~/.*4.*/:
            button4Handlers()
            break
            case ~/.*5.*/:
            button5Handlers()
            break
            case ~/.*6.*/:
            button6Handlers()
            break
            case ~/.*7.*/:
            button7Handlers()
            break
            case ~/.*8.*/:
            button8Handlers()
            break
        }
    } else if (firstEventId == 0) {
        log.debug "Kicthen Control: No events found. Possible SmartThings latency"
    } else {
        log.debug "Kicthen Control: Duplicate button press found. Not executing handlers"
    }    
}

def button1Handlers() {
	log.debug "Kitchen Control: Button 1 Handler"

// On 
	comptoir.setLevel(100)

	atomicState.noPontEvent = true
	pont.setLevel(100)
    
    table.setLevel(100)

// Off
	atomicState.noTopEvent = true
	top.off()
    
    atomicState.noPlafondEvent = true
    plafond.off()
    
    strip.off()
	dresser.off()
}

def button2Handlers() {
	log.debug "Kitchen Control: Button 2 Handler"

	shades.close()
	location.helloHome.execute("All Off")
}

def button3Handlers() {
	log.debug "Kitchen Control: Button 3 Handler"

// On
	atomicState.noPontEvent = true
    pont.setLevel(40)
    
    table.setLevel(5)
    dresser.setLevel(100)
	
    strip.offBlue()
    strip.offRed()
    strip.offGreen()
    strip.setWhiteLevel(40)

// Off
	comptoir.off()
    
    atomicState.noTopEvent = true
	top.off()
    
    atomicState.noPlafondEvent = true
    plafond.off()
}

def button4Handlers() {
	log.debug "Kitchen Control: Button 4 Handler"
    
    portelock.lock()    
}

def button5Handlers() {
	log.debug "Kitchen Control: Button 5 Handler"

//  Off 
	comptoir.off()
    table.off()
    
    atomicState.noPontEvent = true
    pont.off()

// On
	atomicState.noTopEvent = true
	top.setColor(hex: "#1F2440")
    
    atomicState.noPlafondEvent = true
	plafond.on()
    
	strip.whiteOff()
	strip.setColor(hex: "#1F2440")
    
    dresser.setLevel(25)
}

def button6Handlers() {
	log.debug "Kitchen Control: Button 6 Handler"
	
	if (outside.currentValue("switch") == "off") {
    	outside.on()
    } else {
        outside.off()
    }
}

def button7Handlers() {
	log.debug "Kitchen Control: Button 7 Handler"

	def lockstatus = portelock.currentValue("lock")
	log.debug "Kitchen Control: Lock Status -> $lockstatus"
    
    if (lockstatus == "locked") {
    	log.debug "Kitchen Control: Try to unlock"
    	portelock.unlock()
	} else {
		log.debug "Kitchen Control: Try to lock"
    	portelock.lock()
    }
}

def button8Handlers() {
	log.debug "Kitchen Control: Virtual Button 8 Handler"

// On
	atomicState.noPontEvent = true
	pont.setLevel(100)
    comptoir.setLevel(100)

// Off
    table.off()
    
    atomicState.noPlafondEvent = true
    plafond.off()
    
    atomicState.noTopEvent = true
    top.off()
    
    strip.off()
    dresser.off()
}    

def pontEvent(evt) {
	log.debug "Kicthen Control: Pont - $evt.name: $evt.value"
	
	if (atomicState.noPontEvent) {
    	log.debug "Kicthen Control: Event sent by the app, nothing to do"        
        atomicState.noPontEvent = false
        
    } else {
       	if (evt.value == "on") {
        	log.debug "Kicthen Control: Turn On strip and Dresser"
            
            strip.offBlue()
            strip.offRed()
            strip.offGreen()
			strip.setLevelWhite(100)
            
            dresser.setLevel(100)
		} else {
        	log.debug "Kicthen Control: Turn Off strip and Dresser"

			strip.off()
            dresser.off()
        }
    }
}

def plafondEvent(evt) {
	log.debug "Kicthen Control: Plafond - $evt.name: $evt.value"
    def messages = "Kitchen Control: Switch Planfond - $evt.name: $evt.value"
	   
	if (atomicState.noPlafondEvent) {
    	log.debug "Kicthen Control: Event sent by the app, nothing to do"
    	atomicState.noPlafondEvent = false
        messages = messages + "\nSend by Apps - nothing to do"
        
	} else {
	    atomicState.noTopEvent = true
        
    	if (evt.value == "on") {
        	log.debug "Kicthen Control: Turn on Plafond Couleur"
            
            top.setColor(hex: "#FFFFFF")
            //top.warmWhite()
            
            messages = messages + "\nLed - Set at WarmWhite"
        } else {
        	log.debug "Kicthen Control: Turn Off Plafond Couleur" 
            
            top.off()
            messages = messages + "\nLed - Off"
        }
    }
    
    sendNotificationToContacts(messages, recipients)
}

def topEvent(evt) {
	log.debug "Kicthen Control: Top - $evt.name: $evt.value"
    def messages = "Kitchen Control: Led - $evt.name: $evt.value"
    
    if (atomicState.noTopEvent) {
    	log.debug "Kicthen Control: Event sent by the app, nothing to do"        
        atomicState.noTopEvent = false     
        messages = messages + "\nSend by Apps - nothing to do"
    } else {
	    atomicState.noPlafondEvent = true

		if (evt.value == "on") {
        	log.debug "Kicthen Control: Turn on Plafond switch"
            
            plafond.on()
			messages = messages + "\nSwitch Planfond - Set to On"
        } else {
        	log.debug "Kicthen Control: Turn Off Plafond"
            
            plafond.off()
   			messages = messages + "\nSwitch Planfond - Set to Off"
        }
    }
    
    sendNotificationToContacts(messages, recipients)
}

def visitorEvent(evt) {
	log.debug "Kicthen Control: Visitor - $evt.name: $evt.value"

	sendNotificationEvent("Visitor Switch set to $evt.value")
}

def virtualLevelEvent(evt) {
	log.debug "Kitchen Control: Virtual Controller - $evt.name: $evt.value"
  
   if (atomicState.noVirtualEvent) {
    	log.debug "Kicthen Control: Event sent by the app, nothing to do"        
        atomicState.noVirtualEvent = false     
    } else {
        def level = Integer.parseInt(evt.value)

        if (level >= 10 && level <= 19) {
        	if (level == 10) {
            	atomicState.noVirtualEvent = true
                virtualController.setLevel(11)
            }
            button1Handlers()
        } else if (level >= 20 && level <= 29) {
        	if (level == 20) {
            	atomicState.noVirtualEvent = true
                virtualController.setLevel(21)
            }
            button2Handlers()
        } else if (level >= 30 && level <= 39) {
        	if (level == 30) {
            	atomicState.noVirtualEvent = true
                virtualController.setLevel(31)
            }
            button3Handlers()
        } else if (level >= 40 && level <= 49) {
        	if (level == 40) {
            	atomicState.noVirtualEvent = true
                virtualController.setLevel(41)
            }
            button4Handlers()
        } else if (level >= 50 && level <= 59) {
        	if (level == 50) {
            	atomicState.noVirtualEvent = true
                virtualController.setLevel(51)
            }
            button5Handlers()
        } else if (level >= 60 && level <= 69) {
        	if (level == 60) {
            	atomicState.noVirtualEvent = true
                virtualController.setLevel(61)
            }
            button6Handlers()
        } else if (level >= 70 && level <= 79) {
        	if (level == 70) {
            	atomicState.noVirtualEvent = true
                virtualController.setLevel(71)
            }
            button7Handlers()
        } else if (level == 02) {
            colorChangeMode(1) // Christmass
        } else if (level == 03) {
            colorChangeMode(2) // Frozen
        } else if (level == 04) { 
            colorChangeMode(3) // Spider Man
        } else if (level == 05) { 
            colorChangeMode(4) // Hulk
        } else if (level == 06) { 
            colorChangeMode(5) // Pastel
        }    
	}
}

def virtualOffEvent(evt) {
	log.debug "Kitchen Control: Virtual Controller - $evt.name: $evt.value"

	if (evt.value == "off") {
    
    	if (atomicState.colorChangeMode != 0) {
    		atomicState.colorChangeMode = 0
            strip.off()
            top.off()
        }
        
        atomicState.noTopEvent = false     
        atomicState.noPlafondEvent = false
        atomicState.noPontEvent = false
    }
}

def colorChangeMode(mode) {

	log.debug "Kitchen Control: Color Change Mode: $mode"

	atomicState.inKitchenEvent = true
    
// Off 
	comptoir.off()
    pont.off()
    table.off()
	    
//On
	plafond.on()
//

	atomicState.colorChangeMode = mode
	atomicState.currentColorNumber = -1
    
    if (mode == 5) {
    	strip.onWhite()
    }
    
	colorChangeSwitch()

	atomicState.inKitchenEvent = false    
}

def colorChangeSwitch() {

    log.debug "Color change Switch"

    def mode = atomicState.colorChangeMode

    if (mode != 0) {
        def currentColor = getNextColor(mode)

        if (currentColor) {
            top.setColor(hex: currentColor)
            strip.setColor(hex: currentColor)    
            runIn(60, colorChangeSwitch, [overwrite: false])
        } else { 
            log.debug "No color found - stop"
        }
    } else {
        log.debug "Mode unset - stop"
    }
}

def getNextColor(mode) {
	
    log.debug "Mode [$mode]"
    
    def newColor = atomicState.currentColorNumber + 1
  	def colorToReturn 
    
    log.debug "Color Current[$atomicState.currentColorNumber] New[$newColor]"

    if (mode == 1) { // Christmass
        if (newColor > 1) {
            newColor = 0
        }

        switch (newColor) {
            case 0 : colorToReturn = "#ff0000"; break
            case 1 : colorToReturn = "#00ff00"; break
        }
    } else if (mode == 2) { // Frozen 
        if (newColor > 3) {
            newColor = 0
        }
        switch (newColor) {
            case 0 : colorToReturn = "#C6F2FC"; break
            case 1 : colorToReturn = "#A9E4F9"; break
            case 2 : colorToReturn = "#F47DA5"; break
            case 3 : colorToReturn = "#C47FB6"; break
        }
    } else if (mode == 3) { // SpiderMan 
        if (newColor > 3) {
            newColor = 0
        }
        switch (newColor) {
            case 0 : colorToReturn = "#3E5A70"; break
            case 1 : colorToReturn = "#314256"; break
            case 2 : colorToReturn = "#973C3C"; break
            case 3 : colorToReturn = "#713E3E"; break
        }
    } else if (mode == 4) { // Hulk 
        if (newColor > 4) {
            newColor = 0
        }
        switch (newColor) {
            case 0 : colorToReturn = "#8BFA4D"; break
            case 1 : colorToReturn = "#49FF07"; break
            case 2 : colorToReturn = "#38E92E"; break
            case 3 : colorToReturn = "#8A2C9A"; break
        }
    } else if (mode == 5) { // Pastel 
        if (newColor > 4) {
            newColor = 0
        }
        switch (newColor) {
            case 0 : colorToReturn = "#93DFB8"; break
            case 1 : colorToReturn = "#FFC8BA"; break
            case 2 : colorToReturn = "#E3AAD6"; break
            case 3 : colorToReturn = "#B5D8EB"; break
            case 4 : colorToReturn = "#FFBDD8"; break
        }
    }

	atomicState.currentColorNumber = newColor
    
    return colorToReturn
}