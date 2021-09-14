<img src="https://user-images.githubusercontent.com/62053304/128657744-924b1f39-05cd-46a9-8f4d-2412ec6f38af.png" align="center"/>

# Google Summer of Code 2021 Work Product

**Student** : [Anubhav Sharma](https://github.com/maanuanubhav999)

**Project** : [Research on Peer to peer data transfer](https://summerofcode.withgoogle.com/projects/#5382955624562688)

**Organization** : [Google FHIR SDK](https://summerofcode.withgoogle.com/organizations/6201077587771392)

**Mentor** : [Ephraim Kigamba](https://github.com/ekigamba)

## Android FHIR SDK Peer to Peer transfer (Proof of Concept)


The Android FHIR SDK currently does not supports transferring of data without the internet(Offline capabilities), as the app is intended to be used in areas that can be far from network reach, and in this condition, we wanted to transfer the data without the internet to another device.

By working on this project, the data could be shared to a nearby device without having a dependence on the internet. Currently, for GSoC, we have limited ourselves to researching and exploring various methods available to us.

**Issue** : [#258: [R&D] Peer to peer sync capabilities](https://github.com/google/android-fhir/issues/258)

### What work was done
We have worked on two demo applications which were based on wifi-direct API and nearby connections API-based library and tried to draw the comparison.

These applications were then tested for transfer, connectivity, etc., and the analysis of the basics is written.


**Research Goals**
- Research on various technologies that would serve as the base for further development. 
- Develop Proof of Concept for different technologies that emerge in Research.
- Further documentation regarding the Detailed Research can be found at [Research Document](https://docs.google.com/document/d/1o9m1vUh2tkAuzKS-JIFEohUHeVWeas36ob0ztOlBF0A/edit?usp=sharing)
- Sample applications regarding the research are attached below.
- [Research Guidelines](https://docs.google.com/document/d/1HpoeKvWp77w1S5-YHiezBdGyxVf6TsCDmmLjclLSj70/edit)

**Sample Applications**

 [**Wifi-Direct Sample/demo**](https://github.com/ekigamba/wifi-direct-sample)
 <img align="right" src = "https://user-images.githubusercontent.com/26630009/129835962-8cbd871b-3520-45c1-a14e-28afca3136bc.png" width="200">
 - This application is based on Wifi-direct and the project was originally forked from [Google Git](https://android.googlesource.com/platform/development/+/master/samples/WiFiDirectDemo/)
- For further reference on the Technology [Wifip2p](https://developer.android.com/guide/topics/connectivity/wifip2p#api-overview) 
- Application APK can be [ found here](https://drive.google.com/file/d/1gy_s6wqhi8_I3MlKPR8nGldyUmY7yshK/view?usp=sharing)
- Location needs to switch on, otherwise will throw  **Discovery failed: 0**
- Advantages
    - Faster than Android P2p Sync, Since Nearby Connection API may fall back to Bluetooth for transfer
    - Does not require an intermediate access point.
    - Can handle large data easily
    - Encrypted WPA2
    - During testing, tranfer speed varies between 2-3 MB
- Disadvantages
    - Since we are using the API there is a lot of boiler code to be written, many edge cases to be covered.
    - Implementation is time-consuming.
    - Manual configuration changes for Wi-Fi-direct.

<br clear="right"/>

[**Android-P2p-Sync**](https://github.com/maanuanubhav999/android-fhir/tree/ma/GsocPeertoPeerSync)

<img align="right" src="https://user-images.githubusercontent.com/26630009/129836010-344f0e1a-8523-4f06-9055-18902ff97b98.png" width="200">

- This application is based on [Nearby Connection api](https://developers.google.com/nearby/connections/overview) which is further developed into a library by [OpenSRP.](https://github.com/opensrp/android-p2p-sync)
- For testing purposes, **Android FHIR SDK reference** was integrated with the **android-p2p-sync** library can be [Found here](https://github.com/maanuanubhav999/android-fhir/tree/ma/GsocPeertoPeerSync)
- While integrating the library there were few issues with Jacoco and hence Jacoco was removed, attaching the link for [intrigratable branch](https://github.com/maanuanubhav999/android-p2p-sync/tree/ma/jacocoIssueRemoved) of android-p2p-sync library.
- Application APK can be [found here](https://drive.google.com/file/d/1KtE4eYfpBOLFYiZaQJ7PPl4mdVWcyB-R/view?usp=sharing)
- Advantages
    - Easy to integrate to SDK.
    - Library already has its Database and it keeps track of the last record that has been send and thus all the capabilities that are required for syncing are satisfied.
    - Reduces steps like manual switching wifi/ Bluetooth. 
    - Authentication before transfer.
    - Library's Database is encrypted hence secure.
- Disadvantages  
    - Sometimes the devices will not connect. (Reason not known)
    - Technology seems like a black box since the developer cannot decide how the data will be transferred, or on what basis it will use Bluetooth / wifi.
    - As Nearby Connection can fall back to using Bluetooth, sending larger files may take a long time.

<br clear="right"/>


### To do
Decide on the technology to use and further develop and integrate it with SDK.

