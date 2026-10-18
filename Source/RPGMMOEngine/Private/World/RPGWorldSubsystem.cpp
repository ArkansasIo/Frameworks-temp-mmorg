#include "World/RPGWorldSubsystem.h"

bool URPGWorldSubsystem::RegisterRoom(const FRPGWorldRoom& Room)
{
    if (Room.RoomId < 0) return false;
    Rooms.Add(Room.RoomId, Room);
    return true;
}

bool URPGWorldSubsystem::RemoveRoom(int32 RoomId)
{
    return Rooms.Remove(RoomId) > 0;
}

bool URPGWorldSubsystem::GetRoom(int32 RoomId, FRPGWorldRoom& OutRoom) const
{
    const FRPGWorldRoom* Found = Rooms.Find(RoomId);
    if (!Found) return false;
    OutRoom = *Found;
    return true;
}

bool URPGWorldSubsystem::CanMove(int32 RoomId, FName Direction) const
{
    return GetDestination(RoomId, Direction) >= 0;
}

int32 URPGWorldSubsystem::GetDestination(int32 RoomId, FName Direction) const
{
    const FRPGWorldRoom* Room = Rooms.Find(RoomId);
    if (!Room) return -1;
    const int32* Destination = Room->Exits.Find(Direction);
    return Destination ? *Destination : -1;
}

bool URPGWorldSubsystem::MoveCharacter(AActor* Character, FName Direction)
{
    if (!Character) return false;

    int32& CurrentRoom = CharacterRooms.FindOrAdd(Character, 0);
    const int32 Destination = GetDestination(CurrentRoom, Direction);
    if (Destination < 0) return false;

    const int32 PreviousRoom = CurrentRoom;
    CurrentRoom = Destination;
    OnRoomChanged.Broadcast(PreviousRoom, Destination);
    return true;
}
