 async  function signIn(){
    
     const user_dto = {
       
         email:document.getElementById("email").value,
         password:document.getElementById("password").value,
     }
  
   const response = await  fetch(
           "SignIn",
   {
       method:"POST",
       body:JSON.stringify(user_dto),
       headers:{
          "Content_type": "application/json"
       }
   }
     );
     if(response.ok){
         const json =await response.json();
        if(json.success){
           window.location = "index.html";
        }else{
            if(json.content ==="Unverified"){
                       window.location = "verification.html";
            }
            document.getElementById("massage").innerHTML = json.content;
        }
     }else{
         document.getElementById("massage").innerHTML = "try agun leter";
     }
}