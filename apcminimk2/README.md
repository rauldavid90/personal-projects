# Akai APC Mini Mk2 - Midi Translator

I wrote a Python script that makes the Akai APC Mini mk2 control a LumiDMX project. It simulates the buttons and sliders behavior and sends signals through a virtual midi port in order to trigger the expected events. This version of it contains also a stand-by mode that blocks any signal transmitted from the controller and colors randomly the LEDs of the buttons.

The script can be adapted to work with pretty any program that can be controlled with midi events.

I am using loopMidi to create virtual midi ports in order to create the connection midi controller to software and the PyGame library for the midi translations. 

I successfully distributed this software to many users of LumiDMX by creating an executable containing all the libraries needed.

The functionality of the software is presented in this [YouTube Video]([https://www.youtube.com/watch?v=1LRa_oV5eXA&t=25s]).

<img width="1680" alt="Screenshot 2024-01-29 at 13 09 04" src="https://github.com/rauldavid90/personal-projects/assets/100197393/e794aa9c-9aa6-414d-a9b6-cfb3b6e83b9d">
