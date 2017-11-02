# TimeLapse
A time lapse app for Sony Alpha cameras using the [OpenMemories: Framework](https://github.com/ma1co/OpenMemories-Framework).

I have only a a6300 to test it, if you have another camera I would be happy to receive bug reports.

## Installation ##
Use [Sony-PMCA-RE](https://github.com/ma1co/Sony-PMCA-RE) or install through [sony-pmca.appspot.com](https://sony-pmca.appspot.com/apps).

Thanks to [ma1co](https://github.com/ma1co) for creating this amazing framework and [obs1dium](https://github.com/obs1dium), I used FocusBracket as a code base.

## Usage ##
The app is easy to use. It doesn't have any controls for shutter speed, aperture, ISO, picture quality etc. Adjust all this settings before starting the app, it will use them. If you don't want the camera to focus before each shot, set the camera to manual mode.

Then start the app set the shoot interval and the amount of pictures it should take. Below the seek bars you can see how long it will take to shoot all the photos and how long the video will be. The fps setting only changes the calculation of the video length, the app doesn't produce a video.

Finally click the start button and wait.

You can stop by clicking the MENU button on the camera.

## Known issues ##
Doesn't work on cameras that do not have silent shutter mode. I'm working on a fix for this problem.
Incomplete list of affected cameras:
 - a6000 
 - RX100M3 
