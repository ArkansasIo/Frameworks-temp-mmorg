#include "Commands/RPGCommandRouter.h"

bool URPGCommandRouter::RegisterCommand(FName Command, const FRPGCommandHandler& Handler)
{
    if (Command.IsNone() || !Handler.IsBound()) return false;
    Commands.Add(Command.ToString().ToLower(), Handler);
    return true;
}

bool URPGCommandRouter::ExecuteCommand(AActor* Executor, const FString& Input)
{
    FString CommandText;
    FString Arguments;
    Input.TrimStartAndEnd().Split(TEXT(" "), &CommandText, &Arguments);
    if (CommandText.IsEmpty()) return false;

    const FRPGCommandHandler* Handler = Commands.Find(FName(*CommandText.ToLower()));
    if (!Handler || !Handler->IsBound()) return false;
    return Handler->Execute(Executor, Arguments);
}

bool URPGCommandRouter::HasCommand(FName Command) const
{
    return Commands.Contains(FName(*Command.ToString().ToLower()));
}

void URPGCommandRouter::ClearCommands()
{
    Commands.Empty();
}
