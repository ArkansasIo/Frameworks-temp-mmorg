#include "Persistence/RPGPersistenceSubsystem.h"
bool URPGPersistenceSubsystem::SaveCharacter(FName CharacterId,const FString&Json){if(CharacterId.IsNone())return false;CharacterData.Add(CharacterId,Json);return true;}
bool URPGPersistenceSubsystem::LoadCharacter(FName CharacterId,FString&Json)const{const FString*Found=CharacterData.Find(CharacterId);if(!Found)return false;Json=*Found;return true;}
