function Mammal(species, defining_characteristic) {
  this.species = species;
  this.defining_characteristic = defining_characteristic;
 }
 
Mammal.prototype.print = function(){
  return "Hello I'm a " + this.species + ". " +
         "I have " + this.defining_characteristic + "."
}