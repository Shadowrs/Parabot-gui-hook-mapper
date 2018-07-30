# Parabot-gui-hook-mapper
Assesses a given Parabot API JAR along with a Client JAR and filters possible classes and field types that match the API. 


<h3>Contributing</h4>

<h5>Code Style</h5>

Not enforced
  
<h5>Building</h5>

Built with JDK 10.0.1
  
<h5>Dependencies</h5>
  
![dependencies](https://i.imgur.com/karXsGZ.jpg)

<h1>GUI</h1>

**1.** When opened, you need to click "Select JAR" and browse using file explorer to the Parabot-API-[type=317-minified].jar and [client=dreamscape].jar

![initial looks](https://i.imgur.com/Nsvu9sr.jpg)

**2.** Then click "Load JAR" for each. Accessors from the API along with all the client's classes are displayed. Select one from each list, then right click and "Link to [x]"

![linking API Accessors to client classes](https://i.imgur.com/MMOZKQj.jpg)

**3.** When all Accessors have a client class binding, move to tab 2 - "Getters"

![inital tab 2: getters tab pane](https://i.imgur.com/k3Uaogm.jpg)

**4.** Select which Accessor contains the fields you want to assign Getters to.

![getters: available API accessors](https://i.imgur.com/qcUu3dZ.jpg)

**5.** Finally, pick from either list which field you're assigning a Getter to. 

![getters: available client class fields that match the chosen accessor field type](https://i.imgur.com/N9ACDJ9.jpg)

![getters: ALL available client class fields](https://i.imgur.com/FC0tW83.jpg)
