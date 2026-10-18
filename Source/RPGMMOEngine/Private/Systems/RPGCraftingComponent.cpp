#include "Systems/RPGCraftingComponent.h"
bool URPGCraftingComponent::Craft(const FRPGRecipe& Recipe){return HasRecipe(Recipe.RecipeId)&&!Recipe.ResultItemId.IsNone()&&Recipe.ResultQuantity>0;}
void URPGCraftingComponent::RegisterRecipe(const FRPGRecipe& Recipe){if(Recipe.RecipeId.IsNone())return;for(FRPGRecipe&R:Recipes)if(R.RecipeId==Recipe.RecipeId){R=Recipe;return;}Recipes.Add(Recipe);}
bool URPGCraftingComponent::HasRecipe(FName RecipeId)const{return Recipes.ContainsByPredicate([RecipeId](const FRPGRecipe&R){return R.RecipeId==RecipeId;});}
