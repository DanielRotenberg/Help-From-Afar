# Help-From-Afar
Mobile app which provides physiological assistance based on a set of questions filled out by patient. Additionally, a registered physiologist will review and provide a professional summary with the option to schedule an appointment.



user name and password-
*Patient -carol -  1111,gal -  111,Marcus -  1111
*Therapist -ter 111


Documentation-     
Code Guide Lines-  
 variables names - using google name convention - for instance non public prefix -"m"' for static prefix "s".
 (not including constants)
 class names - all the model classes we used suffix "M", to distinguish from the rest of the application.

OO principles-
* using encapsulation with variables - (private variables and public getters and setters).
* using Single responsibility principle by separating Questionnaire and QuestionnaireBank
* using Aggregation - (Questionnaire and Question)
* using abstraction with Activity and Fragment to enable reusable and clean code. (SingleFragmentActivity, SingleFragment)
* using delegation to compare Objects (Questionnaire and User)
* using static factory for intents to hide functionality from the caller class

Design Patterns-
* using Singleton pattern to verify that its the same User in all parts of application.

MVC-
* the Questionnaire logic separated into model classes
* the User logic separated into model classes
* the communication with server (Parse.com) separated into model class
* all the data about Parse classes and fields structured with usage of constants and inner classes.
 (the architecture building was inspired by Android class R).

Android-
* using ViewPager to reuse Fragments.
* using RecyclerView instead ListView to use the built-in recycling mode.
* reusing the same screens for different type of userMs (Therapist and Patient) and controlling the
  functionality that the app enables by detecting the type of the userM.
* using RegularExpressions to validate the userM (age,name and so on).
* storing and retrieving data on a server. (we used Parse.com as our server).
* using SharedPreference to store personal data in order to minimize the communication with the server.
* using Fragment Arguments -> common approach to prevent tight coupling between Activity and Fragment.
  for more info - http://stackoverflow.com/questions/9245408/best-practice-for-instantiating-a-new-android-fragment

Few important notes -

* class User must not be singleton in order to create list of patients for the therapist and list of therapist for simple user
* for the userM - in order to refer to the same userM in all screens we must create him as singleton, therefor we handle the userM through class
   SingleUser, this class contains User object and also object of ParseMethods.java which responsible on upload/download data to parse.com
* initializing the user type -> user or therapist only once when they log in -> in LoginFragment.java
* !!!!very important - we partly disabled the singleton only for demonstration!!!!

