#include "Characters/RPGCharacterBase.h"
#include "Abilities/RPGAbilityComponent.h"
#include "Combat/RPGCombatComponent.h"
#include "Net/UnrealNetwork.h"
#include "Systems/RPGEffectComponent.h"
#include "Systems/RPGEquipmentComponent.h"
#include "Systems/RPGInventoryComponent.h"
#include "Systems/RPGProgressionComponent.h"
#include "Systems/RPGQuestComponent.h"
#include "Systems/RPGThreatComponent.h"

ARPGCharacterBase::ARPGCharacterBase()
{
    bReplicates = true;
    SetReplicateMovement(true);

    Combat = CreateDefaultSubobject<URPGCombatComponent>(TEXT("Combat"));
    Abilities = CreateDefaultSubobject<URPGAbilityComponent>(TEXT("Abilities"));
    Inventory = CreateDefaultSubobject<URPGInventoryComponent>(TEXT("Inventory"));
    Equipment = CreateDefaultSubobject<URPGEquipmentComponent>(TEXT("Equipment"));
    Progression = CreateDefaultSubobject<URPGProgressionComponent>(TEXT("Progression"));
    Quests = CreateDefaultSubobject<URPGQuestComponent>(TEXT("Quests"));
    Effects = CreateDefaultSubobject<URPGEffectComponent>(TEXT("Effects"));
    Threat = CreateDefaultSubobject<URPGThreatComponent>(TEXT("Threat"));
}

void ARPGCharacterBase::BeginPlay()
{
    Super::BeginPlay();
    RecalculateAttributes();
}

void ARPGCharacterBase::InitializeCharacter(int32 StartingLevel)
{
    Level = FMath::Max(1, StartingLevel);
    Attributes = FRPGAttributeSet();
    Attributes.Strength += (Level - 1) * 1.0f;
    Attributes.Dexterity += (Level - 1) * 1.0f;
    Attributes.Constitution += (Level - 1) * 1.0f;
    Attributes.Intelligence += (Level - 1) * 1.0f;
    Attributes.Vitality += (Level - 1) * 1.0f;
    Attributes.Recalculate();
}

void ARPGCharacterBase::RecalculateAttributes()
{
    Attributes.Recalculate();
    GetCharacterMovement()->MaxWalkSpeed = Attributes.MoveSpeed;
}

void ARPGCharacterBase::GetLifetimeReplicatedProps(TArray<FLifetimeProperty>& OutLifetimeProps) const
{
    Super::GetLifetimeReplicatedProps(OutLifetimeProps);
    DOREPLIFETIME(ARPGCharacterBase, CharacterId);
    DOREPLIFETIME(ARPGCharacterBase, CharacterClass);
    DOREPLIFETIME(ARPGCharacterBase, Level);
    DOREPLIFETIME(ARPGCharacterBase, Attributes);
}
