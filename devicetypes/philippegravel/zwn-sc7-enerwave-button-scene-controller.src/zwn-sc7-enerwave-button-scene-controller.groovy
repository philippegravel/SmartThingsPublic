/**
 *  ZWN-SC7 Enerwave Button Scene Controller
 *
 *	Author: Philippe Gravel with code from Matt Frank based on VRCS Button Controller by Brian Dahlem, based on SmartThings Button Controller
 *	Date Created: 2014-12-18
 *  Last Updated: 2016-03-20
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

 metadata {
  // Automatically generated. Make future change here.
     definition (name: "ZWN-SC7 Enerwave Button Scene Controller", namespace: "philippegravel", author: "Philippe Gravel") {
         capability "Actuator"
         capability "Button"
         capability "Configuration"
         capability "Indicator"
         capability "Sensor"

         command "pushButton1"
         command "pushButton2"
         command "pushButton3"
         command "pushButton4"
         command "pushButton5"
         command "pushButton6"
         command "pushButton7"
         command "virtualPushButton8"

         attribute "currentButton", "STRING"
         attribute "numButtons", "STRING"

         fingerprint deviceId: "0x0202", inClusters:"0x21, 0x2D, 0x85, 0x86, 0x72"
         fingerprint deviceId: "0x0202", inClusters:"0x2D, 0x85, 0x86, 0x72"
     }

     simulator {
         status "button 1 pushed":  "command: 2B01, payload: 01 FF"
         status "button 2 pushed":  "command: 2B01, payload: 02 FF"
         status "button 3 pushed":  "command: 2B01, payload: 03 FF"
         status "button 4 pushed":  "command: 2B01, payload: 04 FF"
         status "button 5 pushed":  "command: 2B01, payload: 05 FF"
         status "button 6 pushed":  "command: 2B01, payload: 06 FF"
         status "button 7 pushed":  "command: 2B01, payload: 07 FF"
         status "button released":  "command: 2C02, payload: 00"
     }

     tiles {
         standardTile("button", "device.button", width: 4, height: 4) {
             state "default", label: " ", icon: "st.unknown.zwave.remote-controller", backgroundColor: "#ffffff"
             state "button 1", label: "1", icon: "st.Weather.weather14", backgroundColor: "#79b821"
             state "button 2", label: "2", icon: "st.Weather.weather14", backgroundColor: "#79b821"
             state "button 3", label: "3", icon: "st.Weather.weather14", backgroundColor: "#79b821"
             state "button 4", label: "4", icon: "st.Weather.weather14", backgroundColor: "#79b821"
             state "button 5", label: "5", icon: "st.Weather.weather14", backgroundColor: "#79b821"
             state "button 6", label: "6", icon: "st.Weather.weather14", backgroundColor: "#79b821"
             state "button 7", label: "7", icon: "st.Weather.weather14", backgroundColor: "#79b821"
             state "button 8", label: "8", icon: "st.Weather.weather14", backgroundColor: "#79b821"
         }

         standardTile("button1", "device.button", inactiveLabel: false, decoration: "flat", width: 1, height: 1) {
             state "default", label:"Travail (1)", action:"pushButton1", icon:"st.Food & Dining.dining1", backgroundColor: "#FFFFFF"
             state "button 1", label:"Travail (1)", action:"pushButton1", icon:"st.Food & Dining.dining1", backgroundColor: "#1E90FF"
         } 
         standardTile("button2", "device.button", inactiveLabel: false, decoration: "flat", width: 1, height: 1) {
             state "default", label:"All Off (2)", action:"pushButton2", icon:"st.Bedroom.bedroom2", backgroundColor: "#FFFFFF"
             state "button 2", label:"All Off (2)", action:"pushButton2", icon:"st.Bedroom.bedroom2", backgroundColor: "#1E90FF"
         } 

         standardTile("button3", "device.button", inactiveLabel: false, decoration: "flat", width: 1, height: 1) {
             state "default", label:"Souper (3)", action:"pushButton3", icon:"st.Food & Dining.dining5", backgroundColor: "#FFFFFF"
             state "button 3", label:"Souper (3)", action:"pushButton3", icon:"st.Food & Dining.dining5", backgroundColor: "#1E90FF"
         } 
         standardTile("button4", "device.button", inactiveLabel: false, decoration: "flat", width: 1, height: 1) {
             state "default", label:" (4)", action:"pushButton4", icon:"st.Weather.weather12", backgroundColor: "#FFFFFF"
             state "button 4", label:" (4)", action:"pushButton4", icon:"st.Weather.weather12", backgroundColor: "#1E90FF"
         } 
         standardTile("button5", "device.button", inactiveLabel: false, decoration: "flat", width: 1, height: 1) {
             state "default", label:"Soiree (5)", action:"pushButton5", icon:"st.Electronics.electronics18", backgroundColor: "#FFFFFF"
             state "button 5", label:"Soiree (5)", action:"pushButton5", icon:"st.Electronics.electronics18", backgroundColor: "#1E90FF"
         } 
         standardTile("button6", "device.button", inactiveLabel: false, decoration: "flat", width: 1, height: 1) {
             state "default", label:"Avant (6)", action:"pushButton6", icon:"st.Home.home2", backgroundColor: "#FFFFFF"
             state "button 6", label:"Avant (6)", action:"pushButton6", icon:"st.Home.home2", backgroundColor: "#1E90FF"
         } 
         standardTile("button7", "device.button", inactiveLabel: false, decoration: "flat", width: 2, height: 1) {
//             state "default", label:"Table (7)", action:"pushButton7", icon:"st.Home.home16", backgroundColor: "#FFFFFF"
//             state "button 7", label:"Table (7)", action:"pushButton7", icon:"st.Home.home16", backgroundColor: "#1E90FF"
             state "default", label:"Porte Lock/Unlock (7)", action:"pushButton7", icon:"st.Home.home3", backgroundColor: "#FFFFFF"
             state "button 7", label:"Porte Lock/Unlock (7)", action:"pushButton7", icon:"st.Home.home3", backgroundColor: "#1E90FF"
		 } 

         standardTile("virtualButton8", "device.button", inactiveLabel: false, decoration: "flat", width: 1, height: 1) {
             state "default", label:"Vaiselle (8)", action:"virtualPushButton8", icon:"st.Kids.kids4", backgroundColor: "#EEEEEE"
             state "button 8", label:"Vaiselle (8)", action:"virtualPushButton8", icon:"st.Kids.kids4", backgroundColor: "#3CFFFF"
         } 

         standardTile("empty", "device.empty", width: 1, height: 3) { }

         main "button"
         details (["button1", "button2", "virtualButton8", "button3", "button4", "empty", "button5", "button6", "button7"])

     }
}

// parse events into attributes
def parse(String description) {
  log.debug "Parsing '${description}'"

    def result = null
  def cmd = zwave.parse(description)
  if (cmd) {
    result = zwaveEvent(cmd)
  }
  return result

}

// Handle a button being pressed
def buttonEvent(button) {
    log.debug "ZWN-SC7 Event: Button #$button"
    button = button as Integer
    def result = []

    updateState("currentButton", "$button")

    if (button > 0) {
        // update the device state, recording the button press
        result << createEvent(name: "button", value: /*"pushed"*/ "button $button", data: [buttonNumber: button], descriptionText: "$device.displayName button $button was pushed", isStateChange: true)

        // turn off the button LED
        result << response(zwave.sceneActuatorConfV1.sceneActuatorConfReport(dimmingDuration: 255, level: 255, sceneId: 0))
    }
    else {
        // update the device state, recording the button press
        result << createEvent(name: "button", value: "default", descriptionText: "$device.displayName button was released", isStateChange: true)

        result << response(zwave.sceneActuatorConfV1.sceneActuatorConfReport(dimmingDuration: 255, level: 255, sceneId: 0))
    }

    result    
}

// A zwave command for a button press was received
def zwaveEvent(physicalgraph.zwave.commands.sceneactivationv1.SceneActivationSet cmd) {

  // The controller likes to repeat the command... ignore repeats
  if (state.lastScene == cmd.sceneId && (state.repeatCount < 4) && (now() - state.repeatStart < 2000)) {
      log.debug "Button ${cmd.sceneId} repeat ${state.repeatCount}x ${now()}"
        state.repeatCount = state.repeatCount + 1
        createEvent([:])
    }
    else {
      // If the button was really pressed, store the new scene and handle the button press
        state.lastScene = cmd.sceneId
        state.lastLevel = 0
        state.repeatCount = 0
        state.repeatStart = now()

        buttonEvent(cmd.sceneId)
    }
}

// A scene command was received -- it's probably scene 0, so treat it like a button release
def zwaveEvent(physicalgraph.zwave.commands.sceneactuatorconfv1.SceneActuatorConfGet cmd) {

  buttonEvent(cmd.sceneId)

}

// The controller sent a scene activation report.  Log it, but it really shouldn't happen.
def zwaveEvent(physicalgraph.zwave.commands.sceneactuatorconfv1.SceneActuatorConfReport cmd) {
    log.debug "Scene activation report"
    log.debug "Scene ${cmd.sceneId} set to ${cmd.level}"

    createEvent([:])
}


// Configuration Reports are replys to configuration value requests... If we knew what configuration parameters
// to request this could be very helpful.
def zwaveEvent(physicalgraph.zwave.commands.configurationv1.ConfigurationReport cmd) {
  createEvent([:])
}

// The VRC supports hail commands, but I haven't seen them.
def zwaveEvent(physicalgraph.zwave.commands.hailv1.Hail cmd) {
    createEvent([name: "hail", value: "hail", descriptionText: "Switch button was pressed", displayed: false])
}

// Update manufacturer information when it is reported
def zwaveEvent(physicalgraph.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
  if (state.manufacturer != cmd.manufacturerName) {
    updateDataValue("manufacturer", cmd.manufacturerName)
  }

    createEvent([:])
}

// Association Groupings Reports tell us how many groupings the device supports.  This equates to the number of
// buttons/scenes in the VRCS
def zwaveEvent(physicalgraph.zwave.commands.associationv2.AssociationGroupingsReport cmd) {
  def response = []

  log.debug "${getDataByName("numButtons")} buttons stored"
  if (getDataByName("numButtons") != "$cmd.supportedGroupings") {
    updateState("numButtons", "$cmd.supportedGroupings")
        log.debug "${cmd.supportedGroupings} groups available"
        response << createEvent(name: "numButtons", value: cmd.supportedGroupings, displayed: false)

        response << associateHub()
  }
    else {
      response << createEvent(name: "numButtons", value: cmd.supportedGroupings, displayed: false)
    }
    return response
}


// Handles all Z-Wave commands we don't know we are interested in
def zwaveEvent(physicalgraph.zwave.Command cmd) {
    createEvent([:])
}

// handle commands

// Create a list of the configuration commands to send to the device
def configurationCmds() {
  // Always check the manufacturer and the number of groupings allowed
  def commands = [
    zwave.manufacturerSpecificV1.manufacturerSpecificGet().format(),
    zwave.associationV1.associationGroupingsGet().format(),
    zwave.sceneControllerConfV1.sceneControllerConfSet(groupId:1, sceneId:1).format(),
    zwave.sceneControllerConfV1.sceneControllerConfSet(groupId:2, sceneId:2).format(),
    zwave.sceneControllerConfV1.sceneControllerConfSet(groupId:3, sceneId:3).format(),
    zwave.sceneControllerConfV1.sceneControllerConfSet(groupId:4, sceneId:4).format(),
    zwave.sceneControllerConfV1.sceneControllerConfSet(groupId:5, sceneId:5).format(),
    zwave.sceneControllerConfV1.sceneControllerConfSet(groupId:6, sceneId:6).format(),
    zwave.sceneControllerConfV1.sceneControllerConfSet(groupId:7, sceneId:7).format()

    ]

    commands << associateHub()

    delayBetween(commands)
}

// Configure the device
def configure() {
  def cmd=configurationCmds()
    log.debug("Sending configuration: ${cmd}")
    return cmd
}


//
// Associate the hub with the buttons on the device, so we will get status updates
def associateHub() {
    def commands = []

    // Loop through all the buttons on the controller
    for (def buttonNum = 1; buttonNum <= integer(getDataByName("numButtons")); buttonNum++) {

          // Associate the hub with the button so we will get status updates
          commands << zwave.associationV1.associationSet(groupingIdentifier: buttonNum, nodeId: zwaveHubNodeId).format()

  }

    return commands
}

// Update State
// Store mode and settings
def updateState(String name, String value) {
  state[name] = value
  device.updateDataValue(name, value)
}

// Get Data By Name
// Given the name of a setting/attribute, lookup the setting's value
def getDataByName(String name) {
  state[name] ?: device.getDataValue(name)
}

//Stupid conversions

// convert a double to an integer
def integer(double v) {
  return v.toInteger()
}

// convert a hex string to integer
def integerhex(String v) {
  if (v == null) {
      return 0
    }

  return Integer.parseInt(v, 16)
}

// convert a hex string to integer
def integer(String v) {
  if (v == null) {
      return 0
    }

  return Integer.parseInt(v)
}

def pushButton1() {
    log.debug "Button 1 Press - In Apps"
    sendEvent(name: "button", value: "button 1", data: [buttonNumber: 1], descriptionText: "$device.displayName button 1 was pushed", isStateChange: true)    
}

def pushButton2() {
    log.debug "Button 2 Press - In Apps"
    sendEvent(name: "button", value: "button 2", data: [buttonNumber: 2], descriptionText: "$device.displayName button 2 was pushed", isStateChange: true)
}

def pushButton3() { 
    log.debug "Button 3 Press - In Apps"
    sendEvent(name: "button", value: "button 3", data: [buttonNumber: 3], descriptionText: "$device.displayName button 3 was pushed", isStateChange: true)
}

def pushButton4() { 
    log.debug "Button 4 Press - In Apps"
    sendEvent(name: "button", value: "button 4", data: [buttonNumber: 4], descriptionText: "$device.displayName button 4 was pushed", isStateChange: true) 
}

def pushButton5() { 
    log.debug "Button 5 Press - In Apps"
    sendEvent(name: "button", value: "button 5", data: [buttonNumber: 5], descriptionText: "$device.displayName button 5 was pushed", isStateChange: true) 
}

def pushButton6() { 
    log.debug "Button 6 Press - In Apps"
    sendEvent(name: "button", value: "button 6", data: [buttonNumber: 6], descriptionText: "$device.displayName button 6 was pushed", isStateChange: true) 
}

def pushButton7() {
    log.debug "Button 7 Press - In Apps"
    sendEvent(name: "button", value: "button 7", data: [buttonNumber: 7], descriptionText: "$device.displayName button 7 was pushed", isStateChange: true)
}

def virtualPushButton8() {
	log.debug "Virtual Button 8 Press - In Apps"
    sendEvent(name: "button", value: "button 8", data: [buttonNumber: 8], descriptionText: "$device.displayName Virtual button 8 was pushed", isStateChange: true)
}
