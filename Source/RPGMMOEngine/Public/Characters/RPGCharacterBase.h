#pragma once

#include "CoreMinimal.h"
#include "GameFramework/Character.h"
#include "Stats/RPGAttributeSet.h"
#include "Characters/RPGCharacterBase.generated.h"

class URPGCombatComponent;
class URPGAbilityComponent;
class URPGInventoryComponent;
class URPGEquipmentComponent;
class URPGProgressionComponent;
class URPGQuestComponent;
class URPGEffectComponent;
class URPGThreatComponent;

UCLASS(Blueprintable)
class RPGMMOENGINE_API ARPGCharacterBase : public ACharacter
{
    GENERATED_BODY()

public:
    ARPGCharacterBase();

    UFUNCTION(BlueprintPure, Category="RPG|Character") const FRPGAttributeSet& GetAttributes() const { return Attributes; }
    UFUNCTION(BlueprintCallable, Category="RPG|Character") void RecalculateAttributes();
    UFUNCTION(BlueprintCallable, Category="RPG|Character") void InitializeCharacter(int32 StartingLevel = 1);

    UPROPERTY(VisibleAnywhere, BlueprintReadOnly, Category="RPG|Components") TObjectPtr<URPGCombatComponent> Combat;
    UPROPERTY(VisibleAnywhere, BlueprintReadOnly, Category="RPG|Components") TObjectPtr<URPGAbilityComponent> Abilities;
    UPROPERTY(VisibleAnywhere, BlueprintReadOnly, Category="RPG|Components") TObjectPtr<URPGInventoryComponent> Inventory;
    UPROPERTY(VisibleAnywhere, BlueprintReadOnly, Category="RPG|Components") TObjectPtr<URPGEquipmentComponent> Equipment;
    UPROPERTY(VisibleAnywhere, BlueprintReadOnly, Category="RPG|Components") TObjectPtr<URPGProgressionComponent> Progression;
    UPROPERTY(VisibleAnywhere, BlueprintReadOnly, Category="RPG|Components") TObjectPtr<URPGQuestComponent> Quests;
    UPROPERTY(VisibleAnywhere, BlueprintReadOnly, Category="RPG|Components") TObjectPtr<URPGEffectComponent> Effects;
    UPROPERTY(VisibleAnywhere, BlueprintReadOnly, Category="RPG|Components") TObjectPtr<URPGThreatComponent> Threat;

protected:
    UPROPERTY(EditAnywhere, Replicated, BlueprintReadOnly, Category="RPG|Character") FName CharacterId;
    UPROPERTY(EditAnywhere, Replicated, BlueprintReadOnly, Category="RPG|Character") FName CharacterClass;
    UPROPERTY(EditAnywhere, Replicated, BlueprintReadOnly, Category="RPG|Character") int32 Level = 1;
    UPROPERTY(EditAnywhere, Replicated, BlueprintReadOnly, Category="RPG|Character") FRPGAttributeSet Attributes;

    virtual void BeginPlay() override;
    virtual void GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const override;
};
