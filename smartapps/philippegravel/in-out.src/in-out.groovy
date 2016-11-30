/**
 *  In/Out
 *
 *  Copyright 2016 Philippe Gravel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "In/Out",
    namespace: "philippegravel",
    author: "Philippe Gravel",
    description: "Set mode when arrive and leaving",
    category: "Family",
    iconUrl: "http://cdn.device-icons.smartthings.com/Home/home3-icn@2x.png",
    iconX2Url: "http://cdn.device-icons.smartthings.com/Home/home3-icn@2x.png",
    iconX3Url: "http://cdn.device-icons.smartthings.com/Home/home3-icn@2x.png"
)

preferences {
	page(name: "selectPeople")
    page(name: "selectThings")
}

def selectPeople() {
	dynamicPage(name: "selectPeople", title: "First, select who", nextPage: "selectThings", uninstall: true) {
        section([mobileOnly:true]) {
            label title: "Assign a name", required: false
        }

		section("When people arrive and leave..."){
			input "peopleToWatch", "capability.presenceSensor", title: "Who?", multiple: true, required: true
		}
        
        section("Delay to keep front light on and unlock door") {
        	input "delay", "number", title: "Number in minutes?", required: true
        }
        
		section("Visitor") {
        	input "visitorSwitch", "capability.switch", title: "Visitor Switch?", required: true
        }

    	section("Send Notifications?") {
        	input("recipients", "contact", title: "Send notifications to", multiple: true, required: false)
    	}
    }
}

def selectThings() {
	dynamicPage(name: "selectThings", title: "Set the things", install: true, uninstall: true) {    
		section() {
			input "avant", "capability.switch", title: "Lumiere Avant", required: true
            input "entree", "capability.switch", title: "Entree", required: true
            input "comptoir", "capability.switch", title: "Comptoir", required: true
            input "portelock", "capability.lock", title: "Porte Avant", required: true
            input "shades", "capability.windowShade", title: "Fenetres", required: true
		}    
	}        
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
    unschedule()
	initialize()
}

def initialize() {

	subscribe(peopleToWatch, "presence", presenseHandler)
}

def presenseHandler(evt) {
	
	log.debug "presenceHandler $evt.name: $evt.value, $evt.displayName"
    def messages = ""

    def now = new Date()
    def sunTime = getSunriseAndSunset()
    log.debug "sunrise and sunset: $sunTime"
    def inNight = (now > sunTime.sunset)
    def delayForLight = false
    def delayForDoor = false
    
    if (evt.value == "not present") {
		log.debug "Someone left"
        messages = "Someone left ($evt.displayName)"

        def presenceValue = peopleToWatch.find{it.currentPresence == "present"}
        if (presenceValue) {
        	log.debug "Still somebody home - nothing to do"
            messages = messages + "\nStill somebody home - nothing to do"
		} else {
        	log.debug "Everybody as left - Do Goodbye!"
            messages = messages + "\nEverybody as left - Do Goodbye!"
            
			if (!visitorAtHome()) {
                portelock.lock()
                shades.close()
                setLocationMode("Away")
            	location.helloHome.execute("All Off")
                messages = messages + "\nAll off, Mode to Away, door lock!"
            } else {
            	messages = messages + "Visitor at home do nothing!"
            }
        } 
	} else {
    	log.debug "Someone arrive"
        messages = "Someone arrive ($evt.displayName)"

		if (notAway()) {
        	log.debug "Somebody already home"
            messages = messages + "\nSomebody already home"
            
            if (inNight && avant.currentValue("switch") == "off") {
            	avant.on()
                delayForLight = true
                messages = messages + "\nOpen light"
            }
            
		} else {
        	log.debug "First arrive - Do Hello!"
            messages = messages + "\nFirst arrive - Do Hello!"
            
            if (inNight) {
                log.debug "Change Mode to Night"
                messages = messages + "\n- Change Mode to Night"
				setLocationMode("Night") 
            } else {
                log.debug "Change Mode to Home"
                messages = messages + "\nChange Mode to Home"
                shades.open()
            	setLocationMode("Home")
            }

			if (inNight) {
            
            	comptoir.setLevel(20)
                entree.setLevel(100)
                
                if (avant.currentValue("switch") == "off") {
                	avant.on()
					delayForLight = true	
                }
            }
        }

        if (inNight) {
			def lockstatus = portelock.currentValue("lock")
    		if (lockstatus == "locked") {
            	delayForDoor = true
            }
        }
    	
        portelock.unlock()
        messages = messages + "\nUnlock door"
        
        if (delayForLight) {
        	if (delayForDoor) {
            	runIn(delay * 60, globalDelay)
                messages = messages + "Set delay for light and door"
            } else {
				runIn(delay * 60, closeSwitchsDelay)
                messages = messages + "Set delay for light Only"                
            }
        } else if (delayForDoor) {
        	runIn(delay * 60, lockDoorDelay)
			messages = messages + "Set delay for door Only"
        }
    }
    
    sendNotificationToContacts(messages, recipients)
}

def globalDelay() {
	lockDoorDelay()
	closeSwitchsDelay()
}

def closeSwitchsDelay() {	
    avant.off()
}

def lockDoorDelay() {
	portelock.lock()
}

def visitorAtHome() {
	return (visitorSwitch.currentSwitch == "on")
}

def notAway() {
	def currMode = location.mode // "Home", "Away", etc.
    log.debug "Not Away - current mode is $currMode"

	return (currMode != "Away")
}
