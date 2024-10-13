 async  function SignUp(){
    
     const user_dto = {
         fisrt_name:document.getElementById("firstName").value,
         last_name:document.getElementById("lastName").value,
         email:document.getElementById("email").value,
         password:document.getElementById("password").value,
     }
  
   const response = await  fetch(
           "SignUp",
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
           window.location = "verification.html";
        }else{
            document.getElementById("massage").innerHTML = json.content;
        }
     }else{
         document.getElementById("massage").innerHTML = "try agun leter";
     }
}

