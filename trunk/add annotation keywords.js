/******************************************************************** 
   Script: add annotation keywords -> for use in iView MediaPro 

   will generate additional search words for each image selected
   -- uses additional java application and mysql
    
       
  Author:      Olga Murdoch,  Febuary 2008
        

*********************************************************************/ 

//========================= Configuration Constants ===========================

var sScriptName = "Show Dates"; 

var bDEBUG = false; 

//================================= Program ===================================

var e = null;  //Error object

// Create ActiveX objects to be used in accessing mediapro 
try { 
   
    // Shell object for alert boxes 
    var shellRef = new ActiveXObject("WScript.Shell"); 
    // FileSystemObject for media files 
    var fileSystemObject = new ActiveXObject("Scripting.FileSystemObject"); 
    // iView MediaPro (starts as server if not already running) 
    var mediaPro = new ActiveXObject("iView.Application");      
} catch(e) { 
    e.description = "Access to ActiveX objects failed.\n" + e.description; 
    throw(e); 
} 

//media items to be processed
var selectedMediaItems = null;

//get the media items to be processed

//make sure there is a catalog open
if(mediaPro.catalogs.count == 0){
	StopMessage("you must have a catalog open");
}else{
	//add all selected items to items
	selectedMediaItems = mediaPro.ActiveCatalog.Selection;
	//make sure there are images selected
	if(selectedMediaItems.count == 0){
		StopMessage("you must select images to use this script");
		selectedMediaItems=null;
	}
}

if(selectedMediaItems != null){
	process(selectedMediaItems);
	launchApplication();
}

//================================= Functions =================================

//process all the selected items
function process(mediaItems){
var fileSystemObject = new ActiveXObject("Scripting.FileSystemObject");
var textFile = fileSystemObject.CreateTextFile("C:\\Info.txt", true);
var numMediaItems = mediaItems.Count;
textFile.WriteLine(numMediaItems); //first print to the file the number of items
	//loop through all the selected items
	 for (var i=1; i<=numMediaItems; i++) { 
	 	var tempItem = mediaItems.Item(i);
        var itemName = (mediaItems.Item(i)).Name;
        var mediaItemAnnotations = (mediaItems.Item(i)).Annotations;
		var mediaItemKeywords = mediaItemAnnotations.Keywords;
		textFile.WriteLine(itemName+" "+mediaItemKeywords); 
   }   
   textFile.Close();	
}

//launch and application on the file system
function launchApplication(){
// launch outside application
//shellRef.Run("C:\\windows\\gcviewer.jar");
shellRef.Run("gcviewer.jar");
}

