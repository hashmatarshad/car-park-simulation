# car-park-simulation
A car park simulation created, designed and coded by Hashmat Arshad in Java using OpenGL LWJGL.

* My submission utilises custom made textures in order to create the car and car park aesthetic design.
 * My submission animates a car reverse bay parking into its desired bay,
 * once the car has parked, the tree and its branch begin growing
 * the sun then travels across two axis to simulate a realistic sundown
 * simultaneously moon begins to travel across two axis as it rises into the scene sky plane
 * once sun is down, the sky changes from day to night.
 * and then using animation across different axis
 * to simulate the car turn out of the bay, and  drive towards the exit,
 * then finally, slowly turn into the open road.
 * This completes the animations in the scene.
 
  <p>Controls:
 * <ul>
 * <li>Press the key 'P' to simulate the parking of the car.
 * <li>Press the key 'E'  to simulate the car driving out the bay and exiting the car park.
 * <li>Please note, you cannot exit the car park until you have parked into a bay first!
 * <li>Press the LEFT and RIGHT arrow keys to move the camera left or right to follow the car.
 * <li>Press the 'Space' key to reset all animations.
 * <li>Press the escape key to exit the application.
 * <li>Hold the x, y and z keys to view the scene along the x, y and z axis, respectively
 * <li>While viewing the scene along the x, y or z axis, use the up and down cursor keys
 *      to increase or decrease the viewpoint's distance from the scene origin
 * </ul>
 
My submission is a parking simulator. The program Student No: 190223933 Module: CS2150
begins with the car at an angle in front of its desired bay. When the user interacts by pressing the “P” key, the program simulates a reverse bay park. This is achieved by animating the car first steering away from the bay and then reversing whilst steering into the bay, by moving the car across both the Z and X axis simultaneously. This simulates a realistic reverse bay park as this is the standard taught in UK driving lessons on how to perform the move(steering away from bay and then reversing). Once the car has fully reversed into the bay, it stops as it has successfully parked.
Following this, the tree and its branch start to grow. This animation is quite advanced as it consists of two cylinder objects translated together to make the tree shape and then grow in opposite directions with their own leafy heads being translated onto the branch as it grows so that it is always connected. Whilst also, the sun travels across two axis to animate a realistic sunset, meanwhile the moon also does the same as it rises into the sky. Once the sun has gone down, the sky changes its texture from day, to night.
The user can then press the “E” key to exit the car park, this is simulated by starting with the car slowly steering out of the bay, then driving and steering towards the car park exit. Once it gets to the end of the car park and to the boundary of the road, it slowly turns into the open road, and so completes the exit and the car park simulation.
NOTES:
• I used three ground planes and three back sky planes instead of scaling and stretching one for both, due to the reason that this allows me to have better texture quality as the image is not stretched.
• Use the arrow keys to move the camera and to be able to follow the car.
Textures: I utilised Illustrator to create almost every texture used in my program. The car park ground texture was created by me, as well as the car head texture and the car window texture.

However, I did use the internet to source my sky textures. References given below:
1. Day Texture Source: Author: Rob DURL: https://www.filterforge.com/filters/421.html
2. Night Texture Source: Author: PogS URL: https://www.filterforge.com/filters/3040.html
 
