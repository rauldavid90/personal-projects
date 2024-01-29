from os import environ
from random import randint
from time import sleep

environ['PYGAME_HIDE_SUPPORT_PROMPT'] = '1'

print("--------------------------")
print("Akai APC Mini MK2 software")
print("--------------------------")

import pygame.midi

# Initialize the pygame.midi module
pygame.midi.init()

# Choose the appropriate input and output devices for your device
in_akai_id = 2
out_akai_id = 6
out_virtual_id = 5

# Open the input and output devices
in_akai = pygame.midi.Input(in_akai_id)
out_akai = pygame.midi.Output(out_akai_id)
out_virtual = pygame.midi.Output(out_virtual_id)

# Settings
brightness = 147  # 143 145 146 (147) 148 149 150


class Button:
    def __init__(self, id, color):
        self.id = id
        self.color = color
        self.status = True

    def turn_on_led(self):
        out_akai.write_short(144, self.id, self.color)  # 144 bottom/right rows
        out_akai.write_short(brightness, self.id, self.color)  # 147 brightness, 150 max brightness, 143 min brightness
        self.status = True

    def turn_off_led(self):
        out_akai.write_short(144, self.id, 0)  # 144 bottom/right rows
        out_akai.write_short(147, self.id, 0)
        self.status = False

    def fade_led(self):
        out_akai.write_short(144, self.id, 2)  # 144 bottom/right rows
        out_akai.write_short(153, self.id, self.color)  # 153 slow blink, 156 strobo
        self.status = False

    def blink_led(self):
        out_akai.write_short(144, self.id, 2)  # 144 bottom/right rows
        out_akai.write_short(159, self.id, self.color)  # 153 slow blink, 156 strobo
        self.status = False

    def change_led_fade(self):
        if self.status:
            self.fade_led()
        else:
            self.turn_on_led()

    def change_led_blink(self):
        if self.status:
            self.blink_led()
        else:
            self.turn_on_led()


class Group:
    def __init__(self, buttons):
        self.buttons = buttons

    def turn_on_all(self):
        for button in self.buttons:
            button.turn_on_led()  # turns on all leds from a group when creating it

    def turn_off_all(self):
        for button in self.buttons:
            button.turn_off_led()

    def turn_on_all_other_than(self, id):
        for button in self.buttons:
            if button.id != id:
                button.turn_on_led()

    def find_button(self, id):
        for button in self.buttons:
            if button.id == id:
                return button


# Colors
off = 0
red = 5
orange = 9
yellow = 13
bright_green = 16
green = 21
kaki = 18
blue = 45
magenta = 95
purple = 49
brown = 61
wood = 127
dark_brown = 126
lavender = 77
warm_white = 93
cold_white = 91
pink = 52
cyan = 78
white = 3
aqua = 79
white_xenon = 91

# button groups

slow_move_buttons = [Button(56, blue)]
slow_move_group = Group(slow_move_buttons)
slow_move_group.turn_on_all()

moves_buttons = [Button(57, brown), Button(58, brown),
                 Button(48, brown), Button(49, brown), Button(50, brown),
                 Button(40, brown), Button(41, dark_brown), Button(42, brown),
                 Button(32, brown), Button(33, dark_brown),
                 Button(24, brown), Button(25, dark_brown),
                 Button(16, brown), Button(17, dark_brown),
                 Button(8, brown), Button(9, brown),
                 Button(0, brown), Button(1, brown)]
moves_group = Group(moves_buttons)
moves_group.turn_on_all()

new_move_1_buttons = [Button(26, wood)]
new_move_1_group = Group(new_move_1_buttons)
new_move_1_group.turn_on_all()
new_move_2_buttons = [Button(18, wood)]
new_move_2_group = Group(new_move_2_buttons)
new_move_2_group.turn_on_all()
new_move_3_buttons = [Button(10, wood)]
new_move_3_group = Group(new_move_3_buttons)
new_move_3_group.turn_on_all()
new_move_4_buttons = [Button(19, wood)]
new_move_4_group = Group(new_move_4_buttons)
new_move_4_group.turn_on_all()
new_move_5_buttons = [Button(11, wood)]
new_move_5_group = Group(new_move_5_buttons)
new_move_5_group.turn_on_all()

beam_colors_buttons = [Button(59, red), Button(60, green), Button(61, blue),
                       Button(51, magenta), Button(52, orange), Button(53, lavender),
                       Button(43, yellow), Button(44, bright_green), Button(45, white),
                       Button(117, green)]  # all colors change
beam_colors_group = Group(beam_colors_buttons)
beam_colors_group.turn_on_all()

ledbar_colors_buttons = [Button(34, white), Button(35, red), Button(36, green), Button(37, blue),
                         Button(27, magenta), Button(28, orange), Button(29, lavender),
                         Button(118, green)]  # all colors change
ledbar_colors_group = Group(ledbar_colors_buttons)
ledbar_colors_group.turn_on_all()

new_effect_1_buttons = [Button(20, kaki)]
new_effect_1_group = Group(new_effect_1_buttons)
new_effect_1_group.turn_on_all()

new_effect_2_buttons = [Button(21, kaki)]
new_effect_2_group = Group(new_effect_2_buttons)
new_effect_2_group.turn_on_all()

new_effect_3_buttons = [Button(12, kaki)]
new_effect_3_group = Group(new_effect_3_buttons)
new_effect_3_group.turn_on_all()

new_effect_4_buttons = [Button(13, kaki)]
new_effect_4_group = Group(new_effect_4_buttons)
new_effect_4_group.turn_on_all()

fixed_positions_buttons = [Button(62, purple), Button(63, purple),
                           Button(54, purple), Button(55, purple),
                           Button(46, purple), Button(47, purple)]
fixed_positions_group = Group(fixed_positions_buttons)
fixed_positions_group.turn_on_all()

onoff_2by2_buttons = [Button(38, red), Button(39, red)]
onoff_2by2_group = Group(onoff_2by2_buttons)
onoff_2by2_group.turn_on_all()

effects_buttons = [Button(30, green), Button(31, green),
                   Button(22, green), Button(23, green),
                   Button(14, green), Button(15, green),
                   Button(6, green), Button(7, green)]
effects_group = Group(effects_buttons)
effects_group.turn_on_all()

# independent toggle buttons will be a group of one button
all_colors_slow_fade_beam_buttons = [Button(114, green)]
all_colors_slow_fade_beam_group = Group(all_colors_slow_fade_beam_buttons)
all_colors_slow_fade_beam_group.turn_on_all()

all_colors_slow_fade_led_buttons = [Button(115, green)]
all_colors_slow_fade_led_group = Group(all_colors_slow_fade_led_buttons)
all_colors_slow_fade_led_group.turn_on_all()

glob_buttons = [Button(5, warm_white)]
glob_group = Group(glob_buttons)
glob_group.turn_on_all()

prism_buttons = [Button(102, red)]
prism_group = Group(prism_buttons)
prism_group.turn_on_all()

# all dependency groups (fade/blink for pads, little buttons will blink in both cases)
toggle_fade_groups = [slow_move_group, moves_group,
                      new_move_1_group, new_move_2_group, new_move_3_group, new_move_4_group, new_move_5_group,
                      new_effect_1_group, new_effect_2_group, new_effect_3_group, new_effect_4_group,
                      fixed_positions_group,
                      onoff_2by2_group,
                      effects_group,
                      all_colors_slow_fade_beam_group,
                      all_colors_slow_fade_led_group,
                      glob_group,
                      prism_group]
toggle_blink_groups = [beam_colors_group,
                       ledbar_colors_group]

# one group for all flash buttons (they don't depend on each other)
shortcuts_buttons = [Button(2, blue), Button(3, blue), Button(4, blue),
                     Button(100, blue), Button(101, blue), Button(103, red), Button(104, red), Button(106, red),
                     Button(122, red)]
shortcuts_group = Group(shortcuts_buttons)
shortcuts_group.turn_on_all()

all_groups = [slow_move_group, moves_group,
              new_move_1_group, new_move_2_group, new_move_3_group, new_move_4_group, new_move_5_group,
              new_effect_1_group, new_effect_2_group, new_effect_3_group, new_effect_4_group,
              fixed_positions_group,
              onoff_2by2_group,
              effects_group,
              all_colors_slow_fade_beam_group,
              all_colors_slow_fade_led_group,
              glob_group,
              prism_group,
              beam_colors_group,
              ledbar_colors_group, shortcuts_group]

fps = 240
clock = pygame.time.Clock()
clicked = 0
active = True
click_count = 1

while True:
    if in_akai.poll():
        midi_message = in_akai.read(1)[0]
        event = midi_message[0][0]
        clicked_button = midi_message[0][1]
        value = midi_message[0][2]
        time = midi_message[1] // 1000 // 60

        if clicked_button == 112 and value == 127:  # button click (for active mode)
            if active:
                active = False
                color = randint(1, 10)
                for id in range(0, 123):
                    if id % 8 == 0:
                        color += randint(10, 15)
                    if id < 100:
                        out_akai.write_short(154, id, color)
                    else:
                        out_akai.write_short(144, id, 0)
            else:
                active = True
                for id in range(0, 123):
                    out_akai.write_short(144, id, 0)
                for group in all_groups:
                    group.turn_on_all()

        if active:
            # print(event, clicked_button, value, time, midi_message)
            if event == 176:  # slider (176)
                slider_value = midi_message[0][2]
                out_virtual.write_short(176, clicked_button, slider_value)
            else:  # pad
                if shortcuts_group.find_button(clicked_button):  # button (unchecked flash button in lumidmx)

                    # signal
                    out_virtual.write_short(147, clicked_button, 32)

                    # led status
                    if event == 144:  # toggle click
                        out_akai.write_short(144, clicked_button, 0)  # bottom row - turn off
                        out_akai.write_short(156, clicked_button, white)  # pads - white strobo

                        # count
                        click_count += 1
                        print(click_count)
                    if event == 128:  # toggle release
                        out_akai.write_short(144, clicked_button, red)  # bottom row - turn on
                        out_akai.write_short(147, clicked_button, blue)  # pads - turn on

                else:  # toggle
                    # signal
                    if clicked == 0:
                        out_virtual.note_on(clicked_button, 127)
                        clicked = 1

                        # count
                        click_count += 1
                        print(click_count)
                    else:
                        out_virtual.note_off(clicked_button)
                        clicked = 0

                    # led status
                    if event == 144:  # toggle click
                        for group in toggle_fade_groups:
                            if group.find_button(clicked_button):
                                group.turn_on_all_other_than(clicked_button)
                        for group in toggle_blink_groups:
                            if group.find_button(clicked_button):
                                group.turn_on_all_other_than(clicked_button)

                    if event == 128:  # toggle release
                        for group in toggle_fade_groups:
                            if group.find_button(clicked_button):
                                group.find_button(clicked_button).change_led_fade()
                        for group in toggle_blink_groups:
                            if group.find_button(clicked_button):
                                group.find_button(clicked_button).change_led_blink()

    clock.tick(fps)

# Close the input and output devices
in_akai.close()
out_akai.close()
out_virtual.close()

# Quit the pygame.midi module
pygame.midi.quit()
