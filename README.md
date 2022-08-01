# RealEstateManager

------------------

## Presentation

RealEstateManager is an app that allows you to manage easily a real estate park.  
  
You can add an estate, edit it and save it in an offline database and access it without internet connection.  
  
The app can be used on a smartphone and a tablet.  

## Process to follow

1. Download the project.  

2. This app uses Google Map's SDK and Positionstack's API, you will then need an API key for each of these.  
* Create an Google Map's API key : [Google Developers - Android SDK](https://developers.google.com/maps/documentation/android-sdk/get-api-key) (a Google account is required)  
* Create a Positionstack's API key : [Positionstack](https://positionstack.com/) (a PositionStack account is required)  

3. Once you have these two API key, you have to copy these in their corresponding fields, in the file *gradle.properties*  

```
MAPS_API_KEY=YOUR_MAP_API_KEY
POSITIONSTACK_API_KEY=YOUR_POSITION_STACK_API_KEY
```

4. The app is now ready to run !  

## App Overview

![Main Screen (List) - Smartphone](https://github.com/HalTobin/P9_RealEstateManager/blob/main/README_res/SP_Home_List.png)
![Main Screen (Map) - Smartphone](https://github.com/HalTobin/P9_RealEstateManager/blob/main/README_res/SP_Home_Map.png)
![Details Screen - Smartphone](https://github.com/HalTobin/P9_RealEstateManager/blob/main/README_res/SP_Details.png)
![Add Or Edit An Real Estate Screen - Smartphone](https://github.com/HalTobin/P9_RealEstateManager/blob/main/README_res/SP_AddEdit.png)
![Search An Real Estate Dialog - Smartphone](https://github.com/HalTobin/P9_RealEstateManager/blob/main/README_res/SP_Home_Search.png)  

![Main Screen - Tablet](https://github.com/HalTobin/P9_RealEstateManager/blob/main/README_res/T_Home.png)
![Details Screen - Tablet](https://github.com/HalTobin/P9_RealEstateManager/blob/main/README_res/T_Details.png)
![Add Or Edit An Real Estate Screen - Tablet](https://github.com/HalTobin/P9_RealEstateManager/blob/main/README_res/T_AddEdit.png)
![Search An Real Estate Dialog - Tablet](https://github.com/HalTobin/P9_RealEstateManager/blob/main/README_res/T_Home_Search.png)   


