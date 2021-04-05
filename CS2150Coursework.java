/* CS2150Coursework.java
 * USERNAME: 190223933 	NAME: Hashmat Arshad 	YEAR 2	COMPUTER SCIENCE CS2150
 * STATEMENT OF OWN WORK: 
 * This is my own work and has been individually created by me, Hashmat Arshad.
 *
 * Scene Graph:
 *	Scene Origin
 *	|
 *	+--[S(25.0, 1.0, 20.0 T(-24.97, -2.5, -10.0)] Left Car Park Ground Plane
 *	|
 *	+--[S(25.0, 1.0, 20.0) T(0.0, -2.5, -10.0)] Middle Car Park Ground Plane
 *	|
 *	+--[S(25.0, 1.0, 20.0) T(24.97, -2.5, -10.0)] Right Car Park Ground Plane
 *	|
 *	+--[S(25.0, 1.0, 10.0) Rx(90) T(-25.0, 2.0, -20.0)] Left Sky Plane
 *	|
 *	+--[S(25.0, 1.0, 10.0) Rx(90) T(0.0, 2.0, -20.0)] Middle Sky Plane
 *	|
 *	+--[S(25.0, 1.0, 10.0) Rx(90) T(25.0, 2.0, -20.0)] Right Sky Plane
 *	|
 *	+--[T(moonX, moonY, -20.0)] Moon
 *	|
 *	+--[T(sunX, sunY, -20.0)] Sun
 *	|
 *	+- - [T(0.0, -2.5, -18.0)] Tree
 *	|	|
 *	|	+- -[Rx(-90)] Trunk
 *	|	|
 *	|	+- -[T(0.0, leafY, 0.0)] Leaves Head for trunk
 *	|	|
 *	|	+—[Rx(-85) Ry(50) T(0, 0.5, 0)] Branch
 *	|	|
 *	|	+- -[T(branchLeafX, branchLeafY, 0.0)] Leaves Head for branch
 *	|
 *	+- - [Rx(rotationAngle) S(1.5, 1.5, -1) T(carMoveX, -0.5, carMoveZ)] Car
 *		|
 *		+- -[Rx(rotationAngle) S(1.5f, 1.5f, -1f) T(carMoveX, -0.5f, carMoveZ)] Car Body
 *		|
 *		+- -[Rx(rotationAngle) S(1.5f, 1.5f, -1f) T(carMoveX, -0.5f, carMoveZ)] Car Wheels
 *
 */
package coursework.HashmatArshad;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;
import GraphicsLab.*;

/**
 * TODO: Briefly describe your submission here
 * COURSEWORK IDEA = CAR PARKING SIMULATOR
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
 * 
 * <p>Controls:
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
 *
 */
public class CS2150Coursework extends GraphicsLab
{
	private float size = 0.15f; //wheel radius
	//display list ids
	private final int firstCObjectList = 1;
	private final int secondCObjectList = 2;
    private final int planeList = 3;
    private final int thirdCWheelList = 4;
    
    private float cameraX = 0.0f; //current camera position
    private float cameraMaxX = 20f; //max value for moving right
    private float cameraMinX =-15f; // minimum value for moving left
    
    private float treeHeight = 3f; // current main tree trunk height 
    private float leafY = 3f; // Y position of main leafy head
    private float branchHeight = 1f; // current branch height
    private float branchLeafY = -1.5f; // Y position of leafy head for branch
    private float branchLeafX = 1.2f; // X position of the leafy head for the branch
    private float maxTreeHeight = 6f; // max height tree can grow
    
    private float sunY = 5.1f; // current sun Y position
    private float sunX = -10.0f; // current sun X position
    private float moonMaxY = 5.1f; // max height moon can rise to
    private float moonMaxX = 7f; // max X position as the moon diagonally rises
    private float moonX = 15f; // current moon X position
    private float moonY = -5f; // current moon Y position
    
    private final float maxRotation = 0f; // car is straight, headlights facing right
    private final float maxRRotation =-70f; // max rotation before reverse park
    private final float maxReverse = -5.2f; // max reverse X value allowed in bay
    
    private boolean parkCar = false; // if true then park the car
    private boolean exitCarPark = false; // exits the car park when true
    private boolean carParked = false; // has the car parked in the bay
    private boolean isSunDown = false;
    
    private float carMoveX = -1.5f; // current X position of the car
    private float carMoveZ = -7f; // current Y position of car
    private float rotationAngle = -90.0f; //current angle the car faces

    //Textures custom made using illustrator, other than the day and night texture
    private Texture groundTextures; // store ground car park middle texture
    private Texture groundLeftTexture; // store ground car park left side texture
    private Texture groundRightTexture; // store ground plane car park right side texture 
    private Texture dayTexture; // stores the day sky texture
    //(Source of day texture: Author: Rob D 	URL: https://www.filterforge.com/filters/421.html)
    private Texture daySidesTexture; // stores the side plane day sky texture
    private Texture nightTexture; // stores the night sky texture
   //(Source of night texture: Author: PogS 	URL: https://www.filterforge.com/filters/3040.html)
    private Texture selectedSkyTexture; //stores the current texture for the sky
    private Texture selectedSideSkyTexture; // stores the current side plane day sky texture
    
    private Texture carWindowTexture; // store the texture for the car windows
    private Texture frontCarHeadlights; // store the texture for the car head lights face
    
    //TODO: Feel free to change the window title and default animation scale here
    public static void main(String args[])
    {   new CS2150Coursework().run(WINDOWED,"CS2150 Coursework: Parking Simulation",0.005f);
    }

    protected void initScene() throws Exception
    {//TODO: Initialise your resources here - might well call other methods you write.
    	groundTextures = loadTexture("coursework/HashmatArshad/Textures/carParkWithExit1.BMP");
    	groundLeftTexture = loadTexture("coursework/HashmatArshad/Textures/carParkLeft.BMP");
    	groundRightTexture = loadTexture("coursework/HashmatArshad/Textures/carParkRight.BMP");
    	nightTexture = loadTexture("coursework/HashmatArshad/Textures/night.BMP");
    	dayTexture = loadTexture("coursework/HashmatArshad/Textures/day.BMP");
    	daySidesTexture = loadTexture("coursework/HashmatArshad/Textures/day.BMP");
    	selectedSideSkyTexture = daySidesTexture;
    	selectedSkyTexture = dayTexture;
    	
    	carWindowTexture = loadTexture("coursework/HashmatArshad/Textures/cWindow1.BMP");
    	frontCarHeadlights = loadTexture("coursework/HashmatArshad/Textures/frontFaceL1.BMP");
    	
    	// global ambient light level quite bright
        float globalAmbient[]   = {0.7f,  0.7f,  0.7f, 1.0f};
        // set the global ambient lighting
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT,FloatBuffer.wrap(globalAmbient));

        // the first light for the scene is bright white light...
        float diffuse0[]  = { 1f,  1f, 1f, 1.0f};
        // ...with a very dim ambient contribution...
        float ambient0[]  = { 0.1f,  0.5f, 0.5f, 1.0f};
        // ...and is positioned above the viewpoint
        float position0[] = { 0.0f, 6.0f, 0.0f, 8.0f};

        // supply OpenGL with the properties for the first light
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, FloatBuffer.wrap(ambient0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, FloatBuffer.wrap(diffuse0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, FloatBuffer.wrap(diffuse0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, FloatBuffer.wrap(position0));
        // enable the first light
        GL11.glEnable(GL11.GL_LIGHT0);
    	
    	//ensure all normals are automatically re-normalised after transformations
        GL11.glEnable(GL11.GL_NORMALIZE);
        //enable lighting calculations
        GL11.glEnable(GL11.GL_LIGHTING);
    	//create display list for ground and background plane
    	GL11.glNewList(planeList,GL11.GL_COMPILE);
        {   
        	drawUnitPlane();
        }
        GL11.glEndList();
        //create display  list to draw first car object
        GL11.glNewList(firstCObjectList,GL11.GL_COMPILE);
        {   
        	drawCar(Colour.RED);
        } GL11.glEndList(); 
        //create display list to draw second car object
        GL11.glNewList(secondCObjectList, GL11.GL_COMPILE);
    	{
    		secondCObject(Colour.RED,Colour.RED,Colour.RED,Colour.RED,Colour.RED);
    	} GL11.glEndList();
    	//create display list to draw third car object (the wheels)
    	GL11.glNewList(thirdCWheelList,GL11.GL_COMPILE);
        {   
        	drawWheels(Colour.BLACK);
        } GL11.glEndList(); 
    
        //force draw each face so when animating, prevents disappearance
    	GL11.glDisable(GL11.GL_CULL_FACE); //disable object culling
    }
    protected void checkSceneInput()
    {//TODO: Check for keyboard and mouse input here
    	
    		//check if users presses P key, if yes, park car in bay.
    		if(Keyboard.isKeyDown(Keyboard.KEY_P)) 
    		{
    			parkCar = true;
    		// check if user presses E key, and if the car has parked in a bay
    		// animate exiting car park if yes.
    		} else if(Keyboard.isKeyDown(Keyboard.KEY_E) && carParked)
    		{
    			exitCarPark = true;
    		// reset simulator if user presses space key
    		// reset all animations and modified values
    		} else if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) 
    		{
    			resetAnimations();
    		//check if user presses right arrow key
    		//move camera to the right if pressed
    		} else if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) 
    		{
    			// use cameraMaxX value to prevent camera leaving scene
    			if(cameraX < cameraMaxX) 
    			{
    				cameraX += 0.5f * getAnimationScale();
    			}
    		//check if user presses left arrow key
    		//move camera to the left if pressed
    		} else if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) 
    		{
    			// use cameraMinX value to prevent camera leaving scene
    			if(cameraX > cameraMinX) 
    			{
    				cameraX -= 0.5f * getAnimationScale();
    			}
    		}    		
    }
    protected void updateScene()
    {
        //TODO: Update your scene variables here - remember to use the current animation scale value
        //        (obtained via a call to getAnimationScale()) in your modifications so that your animations
        //        can be made faster or slower depending on the machine you are working on
    	
    	/**
    	 * Rotates car if the boolean value is true
    	 * and the rotationAngle variable is less than MaxRRotation
    	 * loop until rotationAngle is no longer less than maxRRotation
    	 * animates car positioning itself to reverse bay park in front of bay
    	 */
    	
    	if(parkCar && rotationAngle < maxRRotation) 
    	{
    		rotationAngle += 2f * getAnimationScale();
    		carMoveZ += 0.3f * getAnimationScale();
    		carMoveX += 0.3f * getAnimationScale();
    	/**
    	 * The car is animated reversing and steering straight into the bay 
    	 * by modifying X and Z translate position values and its X rotationAngle
    	 * Values are multiplied by animation scale to create smooth animation 
    	 * Speed can be changed if too slow or fast on chosen hardware by modifying
    	 * animation scale value in main method 
    	 */
    	} else if(parkCar && rotationAngle >= maxRRotation && rotationAngle <= maxRotation) 
    	{
    		carMoveZ -= 0.125f * getAnimationScale();
			carMoveX -= 0.2f * getAnimationScale();
			rotationAngle += 2f * getAnimationScale();
    		

			/**
			 * Once car has reversed into bay
			 * Parking is complete
			 * Update boolean carParked so we know the car is parked.
			 */
			if (carMoveX < maxReverse && rotationAngle >= maxRotation) 
			{
				carParked = true;
			}
		//car exits bay and leaves car park
		} else if(exitCarPark) //user presses E after car has parked
		{
				if(carMoveX < 3f && rotationAngle < 70f) 
				{
					//car begins to steer out of bay
	        		rotationAngle += 2f * getAnimationScale();
	        		carMoveX += 0.2f * getAnimationScale();
	        		
				} else if(rotationAngle > 0f && carMoveZ > -14f)
				{
					//steer and drive toward car park exit
					rotationAngle -= 2f * getAnimationScale();
					carMoveX += 0.125f * getAnimationScale();
					carMoveZ -= 0.14f * getAnimationScale();
				} else if(carMoveZ <= -14f && carMoveX <= 20f) 
				{
					//entered exit, drive straight through exit
					carMoveX += 0.25f * getAnimationScale();
		            
				} else if(carMoveX >= 20f && rotationAngle < 60f) 
				{
					//start turning into road slowly 
					rotationAngle += 1f * getAnimationScale();
					carMoveZ -= 0.05f * getAnimationScale();
					carMoveX += 0.04F * getAnimationScale();
					
				// car completes turn into the road and has exited the car park
				} else if(rotationAngle >= 60f && rotationAngle < 90f ) 
				{
					rotationAngle += 2f * getAnimationScale();
				}
		
		}
        // If the car is now parked then...
		else if (carParked)
    	{    		
    		//animate sun going down
    		if (sunX > -19f && sunY > -12) 
    		{
    			sunX -= 0.15f * getAnimationScale();
    			sunY -= 0.15f * getAnimationScale();
    		}
    		// Once sun is almost completely down, change sky texture to night
    		// change isSunDown to true so that the following if statement can make appropriate change
    		if (sunX <= -18f) 
    		{
    			isSunDown = true;
    		}
    		if (isSunDown) 
    		{
    			selectedSkyTexture = nightTexture; //change to night texture
        		selectedSideSkyTexture = nightTexture; //change to night texture
    		}
    		// animate moon rising as sun goes down
    		if (moonX > moonMaxX && moonY < moonMaxY) 
    		{
    			moonX -= 0.15f * getAnimationScale();
    			moonY += 0.15f * getAnimationScale();
    		}
    		// animate tree growth
    		// check if the height of the tree and its leaves is under the maxTreeHeight
    		if (leafY < maxTreeHeight && treeHeight < maxTreeHeight) 
    		{
    			// if it is, simultaneously increase the Y coordinates for both leafY and treeHeight 
    			// creates smooth animation of tree growth
    			leafY += 0.3f * getAnimationScale();
    			treeHeight += 0.3f * getAnimationScale();
    			// increase branch height to animate branch growth
    			// increment/decrement to adjust  branch X position and branch Y position
    			// this allows leafy head to stay attached to the branch throughout animation
    			branchHeight += 0.3f * getAnimationScale();
    			branchLeafX += 0.22f * getAnimationScale();
    			branchLeafY -= 0.1f * getAnimationScale();

    		}
    	} 
    }
    protected void renderScene()
    {//TODO: Render your scene here - remember that a scene graph will help you write this method! 
     //      It will probably call a number of other methods you will write.
    	// Use THREE planes instead of scaling one plane
    	// to avoid stretching texture and have maximum texture quality
  
        //draw left ground plane
        GL11.glPushMatrix();
        {
            // disable lighting calculations so that they don't affect
            // the appearance of the texture 
            GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
            GL11.glDisable(GL11.GL_LIGHTING);
            // change the geometry colour to white so that the texture
            // is bright and details can be seen clearly
            Colour.WHITE.submit();
            // enable texturing and bind an appropriate texture
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,groundLeftTexture.getTextureID());
            
            // position, scale and draw the ground plane using its display list
            GL11.glTranslatef(-24.97f,-2.5f,-10.0f);
            GL11.glScaled(25.0f, 1.0f, 20.0f);
            GL11.glCallList(planeList);

            // disable textures and reset any local lighting changes
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();
        
        // draw the middle ground plane
        GL11.glPushMatrix();
        {
            // disable lighting calculations so that they don't affect
            // the appearance of the texture 
            GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
            GL11.glDisable(GL11.GL_LIGHTING);
            // change the geometry colour to white so that the texture
            // is bright and details can be seen clearly
            Colour.WHITE.submit();
            // enable texturing and bind an appropriate texture
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,groundTextures.getTextureID());
            
            // position, scale and draw the ground plane using its display list
            GL11.glTranslatef(0.0f,-2.5f,-10.0f);
            GL11.glScaled(25.0f, 1.0f, 20.0f);
            GL11.glCallList(planeList);

            // disable textures and reset any local lighting changes
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();
        
        // draw right ground plane - car park exit and road
        GL11.glPushMatrix();
        {
            // disable lighting calculations so that they don't affect
            // the appearance of the texture 
            GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
            GL11.glDisable(GL11.GL_LIGHTING);
            // change the geometry colour to white so that the texture
            // is bright and details can be seen clearly
            Colour.WHITE.submit();
            // enable texturing and bind an appropriate texture
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,groundRightTexture.getTextureID());
            
            // position, scale and draw the ground plane using its display list
            GL11.glTranslatef(24.97f,-2.5f,-10.0f);
            GL11.glScaled(25.0f, 1.0f, 20.0f);
            GL11.glCallList(planeList);

            // disable textures and reset any local lighting changes
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();
        // draw the LEFT back plane and bind to day texture
        GL11.glPushMatrix();
        {
            // disable lighting calculations so that they don't affect
            // the appearance of the texture 
            GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
            GL11.glDisable(GL11.GL_LIGHTING);
            // change the geometry colour to white so that the texture
            // is bright and details can be seen clearly
            Colour.WHITE.submit();
            // enable texturing and bind an appropriate texture
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,selectedSideSkyTexture.getTextureID());
            
            // position, scale and draw the back plane using its display list
            GL11.glTranslatef(-25.0f,2.0f,-20.0f);
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            GL11.glScalef(25.0f, 1.0f, 10.0f);
            GL11.glCallList(planeList);
            
            // disable textures and reset any local lighting changes
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();
        // draw the MIDDLE back plane and bind to day texture
        GL11.glPushMatrix();
        {
            // disable lighting calculations so that they don't affect
            // the appearance of the texture 
            GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
            GL11.glDisable(GL11.GL_LIGHTING);
            // change the geometry colour to white so that the texture
            // is bright and details can be seen clearly
            Colour.WHITE.submit();
            // enable texturing and bind an appropriate texture
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,selectedSkyTexture.getTextureID());
            
            // position, scale and draw the back plane using its display list
            GL11.glTranslatef(0.0f,2.0f,-20.0f);
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            GL11.glScalef(25.0f, 1.0f, 10.0f);
            GL11.glCallList(planeList);
            
            // disable textures and reset any local lighting changes
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();
        // draw the RIGHT back plane and bind to current selected texture (day)
        GL11.glPushMatrix();
        {
            // disable lighting calculations so that they don't affect
            // the appearance of the texture 
            GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
            GL11.glDisable(GL11.GL_LIGHTING);
            // change the geometry colour to white so that the texture
            // is bright and details can be seen clearly
            Colour.WHITE.submit();
            // enable texturing and bind an appropriate texture
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,selectedSideSkyTexture.getTextureID());
            
            // position, scale and draw the back plane using its display list
            GL11.glTranslatef(25.0f,2.0f,-20.0f);
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            GL11.glScalef(25.0f, 1.0f, 10.0f);
            GL11.glCallList(planeList);
            
            // disable textures and reset any local lighting changes
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();
        
        // draw the moon
        GL11.glPushMatrix();
        {
            // how shiny are the front faces of the moon (specular exponent)
            float moonFrontShininess  = 20.0f;
            // specular reflection of the front faces of the moon
            float moonFrontSpecular[] = {0.6f, 0.6f, 0.7f, 1.0f};
            // diffuse reflection of the front faces of the moon
            // bright moon white and shiny with blue hue
            float moonFrontDiffuse[]  = {0.8f, 0.8f, 1f, 1.0f};

            // set the material properties for the sun using OpenGL
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, moonFrontShininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(moonFrontSpecular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(moonFrontDiffuse));

            // position and draw the moon using a sphere quadric object
            GL11.glTranslatef(moonX, moonY, -20.0f);
            new Sphere().draw(0.5f,10,10);
        } GL11.glPopMatrix();
        
        // draw the SUN
        GL11.glPushMatrix();
        {
            // how shiny are the front faces of the moon (specular exponent)
            float sunFrontShininess  = 20.0f;
            // specular reflection of the front faces of the moon
            float sunFrontSpecular[] = {0.8f, 0.8f, 0.0f, 1.0f};
            // diffuse reflection of the front faces of the moon
            float sunFrontDiffuse[]  = {1, 1f, 0.0f, 1.0f};

            // set the material properties for the sun using OpenGL
            // sun is bright yellow 
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, sunFrontShininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(sunFrontSpecular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(sunFrontDiffuse));

            // position and draw the moon using a sphere quadric object
            GL11.glTranslatef(sunX, sunY, -20.0f);
            new Sphere().draw(0.8f,10,10);
        } GL11.glPopMatrix();
        
       // draw the tree
        GL11.glPushMatrix();
        {
        	
            // how shiny are the front faces of the trunk (specular exponent)
            float trunkFrontShininess  = 10.0f;
            // specular reflection of the front faces of the trunk
            float trunkFrontSpecular[] = {0.2f, 0.2f, 0.1f, 1.0f};
            // diffuse reflection of the front faces of the trunk
            float trunkFrontDiffuse[]  = {0.3f, 0.2f, 0.04f, 1.0f};
            //very low ambient trunk light
            float trunkFrontAmbient[]  = {0.01f, 0.05f, 0.0f, 1.0f};

            // set the material properties for the trunk using OpenGL
            // trunk is a low lit brown-green colour
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, trunkFrontShininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(trunkFrontSpecular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(trunkFrontDiffuse));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(trunkFrontAmbient));

            // position the tree
            GL11.glTranslatef(0.0f, -2.5f, -18.0f);
            
            // draw the trunk using a cylinder quadric object. Surround the draw call with a
            // push/pop matrix pair, as the cylinder will originally be aligned with the Z axis
            // and will have to be rotated to align it along the Y axis
            GL11.glPushMatrix();
            {
                GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
                new Cylinder().draw(0.25f, 0.25f, treeHeight, 10, 10);
            }
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            {
            	// draw Branch
            	// rotate so that it protrudes at an angle from the trunk like a real tree
            	// position branch on trunk
            	GL11.glTranslatef(0f, 0.5f, 0f);
                GL11.glRotatef(-85.0f, 1.0f, 0.0f, 0.0f);
                GL11.glRotatef(50.0f, 0.0f, 1f, 0.0f);
                new Cylinder().draw(0.2f, 0.2f, branchHeight, 10, 10);
            }
            GL11.glPopMatrix();

            // how shiny are the front faces of the leafy head of the tree (specular exponent)
            float headFrontShininess  = 20.0f;
            // specular reflection of the front faces of the head
            float headFrontSpecular[] = {0.1f, 0.2f, 0.1f, 1.0f};
            // diffuse reflection of the front faces of the head
            float headFrontDiffuse[]  = {0.0f, 0.7f, 0.0f, 1.0f};
            float headFrontAmbient[] = {0.0f, 0.1f, 0.0f, 1.0f};
            
            // set the material properties for the head using OpenGL
            // head is lighter green colour
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, headFrontShininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(headFrontSpecular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(headFrontDiffuse));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(headFrontAmbient));

            // position and draw the leafy head using a sphere quadric object
            GL11.glTranslatef(0.0f, leafY, 0.0f);
            new Sphere().draw(1.8f, 10, 10);
            
            // position and draw the branch leafy head using a sphere quadric object
            GL11.glTranslatef(branchLeafX, branchLeafY, 0.0f);
            new Sphere().draw(1.2f, 10, 10); //slightly smaller than main leafy head
        }
        GL11.glPopMatrix(); 
        //draw first car
        GL11.glPushMatrix(); 
        {
        	GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        	GL11.glDisable(GL11.GL_LIGHTING);
        	// change the geometry colour to white so that the texture
        	// is bright and details can be seen clearly
        	Colour.WHITE.submit();
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
	        GL11.glBindTexture(GL11.GL_TEXTURE_2D,carWindowTexture.getTextureID());
	        // how shiny are the front faces of the car (specular exponent)
	        float firstCarFrontBackShininess  = 80f;
	        // specular reflection of the front faces of the car
	        float firstCarFrontBackSpecular[] = {0.8f, 0.9f, 0.5f, 1.0f};
	        // diffuse reflection of the front faces of the car
	        float firstCarFrontBackDiffuse[]  = {0.9f, 0.9f, 0.9f, 1.0f};
	        
	        // set the material properties for the car using OpenGL
	        GL11.glMaterialf(GL11.GL_FRONT_AND_BACK, GL11.GL_SHININESS, firstCarFrontBackShininess);
	        GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_SPECULAR, FloatBuffer.wrap(firstCarFrontBackSpecular));
	        GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_DIFFUSE, FloatBuffer.wrap(firstCarFrontBackDiffuse));
	        GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT, FloatBuffer.wrap(firstCarFrontBackDiffuse));
	    	// rotate and position car in front of desired bay at 90 degree angle 
	    	// rotationAngle holds x value for rotation 
	    	// so that the car can be animated rotating, create steer effect as it drives
	        // carMoveX holds car X position, carMoveZ holds Z position
	        // allows car to be animated driving when values updates
	    	GL11.glTranslatef(carMoveX, -0.5f, carMoveZ);
	        GL11.glRotatef(rotationAngle, 0.0f, 1.0f, 0.0f);
	    	GL11.glScalef(1.5f, 1.5f, -1f);
	    	//draw first car object, the top
	    	GL11.glCallList(firstCObjectList);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();

        } 
        GL11.glPopMatrix();
        //draw second and third car objects (with wheels)
        GL11.glPushMatrix(); 
        {
        	GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        	GL11.glDisable(GL11.GL_LIGHTING);
        	// change the geometry colour to white so that the texture
        	// is bright and details can be seen clearly
        	Colour.WHITE.submit();
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
	        GL11.glBindTexture(GL11.GL_TEXTURE_2D,frontCarHeadlights.getTextureID());
	        //car front and back quite shiny
	        float sCarFrontBackShininess  = 80.0f;
	        // specular reflection of the front and back faces of the car
	        float sCarFrontBackSpecular[] = {0.8f, 0.9f, 0.5f, 1.0f};
	        // diffuse reflection of the front faces of the house
	        float sCarFrontBackDiffuse[]  = {0.9f, 0.9f, 0.9f, 1.0f};
	        // set the material properties for the second car object using OpenGL
	        GL11.glMaterialf(GL11.GL_FRONT_AND_BACK, GL11.GL_SHININESS, sCarFrontBackShininess);
	        GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_SPECULAR, FloatBuffer.wrap(sCarFrontBackSpecular));
	        GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_DIFFUSE, FloatBuffer.wrap(sCarFrontBackDiffuse));
	        GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT, FloatBuffer.wrap(sCarFrontBackDiffuse));
	        GL11.glTranslatef(carMoveX, -0.5f, carMoveZ);
	        // rotate and position car in front of desired bay at 90 degree angle 
	    	// rotationAngle holds x value for rotation 
	    	// so that the car can be animated rotating, create steer effect as it drives
	    	GL11.glRotatef(rotationAngle, 0.0f, 1.0f, 0.0f);
	    	GL11.glScalef(1.5f, 1.5f, -1f);
	    	//draw both car body and car wheels
	        GL11.glCallList(secondCObjectList);
	        GL11.glCallList(thirdCWheelList);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();

        } 
        GL11.glPopMatrix();
        
    }
    
    protected void setSceneCamera()
    {
        // call the default behaviour defined in GraphicsLab. This will set a default perspective projection
        // and default camera settings ready for some custom camera positioning below...  
        super.setSceneCamera();
		
        /**
         * using a call to GL11.gluLookAt(...) to place camera directly in front of car
         * camera is slightly high up on y axis and points slightly downward
         * cameraX is used to hold value of x position
         * allow user to move camera sideways if checkSceneInput() detects input 
         * and updates the value
         */
        GLU.gluLookAt(cameraX, 5f, 8f,   // viewer location        
  		      		cameraX, 2f, 0f,    // view point loc.
  		      		0.0f, 1f, 0.0f);   // view-up vector
   }

    protected void cleanupScene()
    {//TODO: Clean up your resources here
    }
    
    /**
     * RESET PARKING SIMULATOR SCENE to start
     * Reset sky texture 
     * Reset floats and boolean values involved in animation
     */
    private void resetAnimations()
    {
    	selectedSkyTexture = dayTexture;
    	selectedSideSkyTexture = daySidesTexture;

        cameraX = 0.0f;
        
        treeHeight = 3f; // current main tree trunk height 
        leafY = 3f; // Y position of main leafy head
        branchHeight = 1f; // current branch height
        branchLeafY = -1.5f; // Y position of leafy head for branch
        branchLeafX = 1.2f; // X position of the leafy head for the branch
        
        sunY = 5.1f;
        sunX = -10.0f;
      
        moonX = 15f;
        moonY = -5f;
        
        parkCar = false;
        exitCarPark = false;
        carParked = false;
        isSunDown = false;

        
        carMoveX = -1.5f;
        carMoveZ = -7f;
        rotationAngle= -90.0f;
        
    }
    
    /**
     * Draws a plane aligned with the X and Z axis, with its front face toward positive Y.
     *  The plane is of unit width and height, and uses the current OpenGL material settings
     *  for its appearance
     */
    private void drawUnitPlane()
    {
    	//define each vertex of plane face
        Vertex v1 = new Vertex(-0.5f, 0.0f,-0.5f); // left,  back
        Vertex v2 = new Vertex( 0.5f, 0.0f,-0.5f); // right, back
        Vertex v3 = new Vertex( 0.5f, 0.0f, 0.5f); // right, front
        Vertex v4 = new Vertex(-0.5f, 0.0f, 0.5f); // left,  front
        
        // draw the plane geometry. order the vertices so that the plane faces up
        GL11.glBegin(GL11.GL_POLYGON);
        { //set normals for face with correct vertex order
            new Normal(v4.toVector(),v3.toVector(),v2.toVector(),v1.toVector()).submit();
            //map appropriate texture to plane
            GL11.glTexCoord2f(0.0f,0.0f);
            v4.submit();
            
            GL11.glTexCoord2f(1.0f,0.0f);
            v3.submit();
            
            GL11.glTexCoord2f(1.0f,1.0f);
            v2.submit();
            
            GL11.glTexCoord2f(0.0f,1.0f);
            v1.submit();
        }
        GL11.glEnd();
        
        // if the user is viewing an axis, then also draw this plane
        // using lines so that axis aligned planes can still be seen
        if(isViewingAxis())
        {
            // also disable textures when drawing as lines
            // so that the lines can be seen more clearly
            GL11.glPushAttrib(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBegin(GL11.GL_LINE_LOOP);
            {
                v4.submit();
                v3.submit();
                v2.submit();
                v1.submit();
            }
            GL11.glEnd();
            GL11.glPopAttrib();
        }
    }
    

    /**
     * draw first car object, top side
     * @param top	stores red colour for top face
     */
    private void drawCar(Colour top) {
    	//define vertex of each face of first car object
    	Vertex v1 = new Vertex(-0.5f, -0.5f,  0.5f);
    	Vertex v2 = new Vertex(-0.3f,  0.1f,  0.5f);
    	Vertex v3 = new Vertex( 0.3f,  0.1f,  0.5f);
    	Vertex v4 = new Vertex( 0.5f, -0.5f,  0.5f);
    	Vertex v5 = new Vertex(-0.5f, -0.5f, -0.5f);
    	Vertex v6 = new Vertex(-0.3f,  0.1f, -0.5f);
    	Vertex v7 = new Vertex( 0.3f,  0.1f, -0.5f);
    	Vertex v8 = new Vertex( 0.5f, -0.5f, -0.5f);
    	
    		// draw the near face:
    		GL11.glBegin(GL11.GL_POLYGON);
    		{
    			//set normals for each face with correct vertex order
    			new Normal(v3.toVector(),v2.toVector(),v1.toVector(),v4.toVector()).submit();
    			//map car window to the near face
    			GL11.glTexCoord2f(0.0f, 0.0f);
    			v3.submit();
    			GL11.glTexCoord2f(1.0f, 0.0f);
    			v2.submit();
    			GL11.glTexCoord2f(1.0f, 1.0f);
    			v1.submit();
    			GL11.glTexCoord2f(0.0f, 1.0f);
    			v4.submit();
    		}
    		GL11.glEnd();

    		// draw the left face:
    		GL11.glBegin(GL11.GL_POLYGON);
    		{
    			//set normals for each face with correct vertex order
    			new Normal(v2.toVector(),v6.toVector(),v5.toVector(),v1.toVector()).submit();
    			//map car window texture to the left face
    			GL11.glTexCoord2f(0.0f, 0.0f);
    			v2.submit();
    			GL11.glTexCoord2f(1.0f, 0.0f);
    			v6.submit();
    			GL11.glTexCoord2f(1.0f, 1.0f);
    			v5.submit();
    			GL11.glTexCoord2f(0.0f, 1.0f);
    			v1.submit();
    		
    		}
    		GL11.glEnd();

    		// draw the right face:
    		GL11.glBegin(GL11.GL_POLYGON);
    		{
    			//set normals for each face with correct vertex order
    			new Normal(v7.toVector(),v3.toVector(),v4.toVector(),v8.toVector()).submit();
    			//map car window texture to the right face
    			GL11.glTexCoord2f(1.0f, 1.0f);
    			v7.submit();
    			GL11.glTexCoord2f(0.0f, 1.0f);
    			v3.submit();
    			GL11.glTexCoord2f(0.0f, 0.0f);
    			v4.submit();
    			GL11.glTexCoord2f(1.0f, 0.0f);
    			v8.submit();
    		}
    		GL11.glEnd();

    		// draw the top face:
    		GL11.glBegin(GL11.GL_POLYGON);
    		{
    			//set normals for each face with correct vertex order
    			new Normal(v7.toVector(),v6.toVector(),v2.toVector(),v3.toVector()).submit();
    			v7.submit();
    			v6.submit();
    			v2.submit();
    			v3.submit();
    		}
    		GL11.glEnd();

    		// draw the bottom face:
    		GL11.glBegin(GL11.GL_POLYGON);
    		{ 
    			//set normals for each face with correct vertex order
    			new Normal(v4.toVector(),v1.toVector(),v5.toVector(),v8.toVector()).submit();
    			v4.submit();
    			v1.submit();
    			v5.submit();
    			v8.submit();
    		}
    		GL11.glEnd(); 

    		// draw the far face:
    		GL11.glBegin(GL11.GL_POLYGON);
    		{
    			//set normals for each face with correct vertex order
    			//maps the car window texture to the far face
    			new Normal(v6.toVector(),v7.toVector(),v8.toVector(),v5.toVector()).submit();
    			GL11.glTexCoord2f(0.0f, 0.0f);
    			v6.submit();
    			GL11.glTexCoord2f(1.0f, 0.0f);
    			v7.submit();
    			GL11.glTexCoord2f(1.0f, 1.0f);
    			v8.submit();
    			GL11.glTexCoord2f(0.0f, 1.0f);
    			v5.submit();
    		}
    		GL11.glEnd();
    }
    /**
     * Draw the second car object, the body
     * @param near	stores red colour for near face
     * @param far	stores red colour for far face
     * @param left	stores red colour for left face
     * @param top	stores red colour for top face
     * @param bottom	stores red colour for bottom face
     */
    private void secondCObject(Colour near, Colour far, Colour left, Colour top, Colour bottom){
       	//defined vertex for each face of second car part/object
    	//coordinates relative to origin
    	Vertex v9 = new Vertex(-0.5f,-1f,0.5f); 
    	Vertex v10 = new Vertex(1f,-1f,0.5f);
    	Vertex v11 = new Vertex(-0.5f,-0.5f,0.5f);
    	Vertex v12 = new Vertex(1f,-0.5f,0.5f);
    	Vertex v13 = new Vertex(1f,-1f,-0.5f);
    	Vertex v14 = new Vertex(1f,-0.5f,-0.5f);
    	Vertex v15 = new Vertex(-0.5f,-1f,-0.5f);
    	Vertex v16 = new Vertex(-0.5f,-0.5f,-0.5f);
    		//near
    		GL11.glBegin(GL11.GL_POLYGON);
    		{
    			//set normals for each face with correct vertex order
    			new Normal(v9.toVector(),v10.toVector(),v12.toVector(),v11.toVector()).submit();
    			near.submit();
    			v9.submit();
    			v10.submit();
    			v12.submit();
    			v11.submit();
    			
    		} GL11.glEnd();
    		
    		//right
    		GL11.glBegin(GL11.GL_POLYGON);
    		{
    			//set normals for each face with correct vertex order
    			new Normal(v13.toVector(), v14.toVector(),v12.toVector(),v10.toVector()).submit();
    			//map headlight texture to car face
    			GL11.glTexCoord2f(0.0f, 0.0f);
    			v10.submit();
    			GL11.glTexCoord2f(1.0f, 0.0f);
    			v12.submit();
    			GL11.glTexCoord2f(1.0f, 1.0f);
    			v14.submit();
    			GL11.glTexCoord2f(0.0f, 1.0f);
    			v13.submit();
    			
    		} GL11.glEnd();
    		//far
    		GL11.glBegin(GL11.GL_POLYGON);
    		{
    			//colour face red
    			far.submit();
    			//set normals for each face with correct vertex order
    			new Normal(v13.toVector(),v14.toVector(),v16.toVector(),v15.toVector()).submit();
    			v13.submit();
    			v14.submit();
    			v16.submit();
    			v15.submit();
    			
    		} GL11.glEnd();
    		//left
    		GL11.glBegin(GL11.GL_POLYGON);
    		{
    			//colour red
    			left.submit();
    			//set normals for each face with correct vertex order
    			new Normal(v16.toVector(),v15.toVector(),v9.toVector(),v11.toVector()).submit();
    			v15.submit();
    			v9.submit();
    			v11.submit();
    			v16.submit();
    			
    		} GL11.glEnd();
    		//bottom
    		GL11.glBegin(GL11.GL_POLYGON);
    		{
    			//colour red
    			bottom.submit();
    			//set normals for each face with correct vertex order
    			new Normal(v10.toVector(),v9.toVector(),v15.toVector(),v13.toVector()).submit();
    			v10.submit();
    			v9.submit();
    			v15.submit();
    			v13.submit();
    			
    		} GL11.glEnd(); 
    		
    		GL11.glBegin(GL11.GL_POLYGON);
    		{
    			//colour red
    			top.submit();
    			//set normals for each face with correct vertex order
    			new Normal(v11.toVector(),v12.toVector(),v14.toVector(),v16.toVector()).submit();
    			v11.submit();
    			v12.submit();
    			v14.submit();
    			v16.submit();
    			
    		} GL11.glEnd(); 
    }
    
    /**
     * Draw third car object, the car wheels
     * @param wheels	stores black colour for wheels
     * 
     */
    private void drawWheels(Colour wheels) {
    		//draw first wheel
    		//translates first wheel into position underneath car at front left
            GL11.glTranslatef(-0.4f, -1.1f, 0.35f);
    		GL11.glPushMatrix();
            {   
            	wheels.submit(); //colour wheels black
            	//create a sphere object for each wheel
            	new Sphere().draw(size,10,10); 
            }
            GL11.glPopMatrix();
            
            //draw second wheel
            //move second wheel into its position at back left
            GL11.glTranslatef(0f, 0f, -0.7f);
    		GL11.glPushMatrix();
            {   new Sphere().draw(size,10,10);
            }
            GL11.glPopMatrix();
            
            //draw third wheel
            //move third wheel into its position at back right
            GL11.glTranslatef(1f, 0f, 0f);
    		GL11.glPushMatrix();
            {   new Sphere().draw(size,10,10);
            }
            GL11.glPopMatrix();
            
            //draw fourth wheel
            //move fourth wheel into its position at the front right
            GL11.glTranslatef(0f, 0f, 0.7f);
    		GL11.glPushMatrix();
            {   new Sphere().draw(size,10,10);
            }
            GL11.glPopMatrix();
    }
    
   
}
//end of program.