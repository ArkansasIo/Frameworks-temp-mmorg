#pragma once
#include <algorithm>
#include <cstdint>
#include <unordered_map>
#include <vector>
namespace rpg::domain {
enum class Stat : std::uint8_t { STR,DEX,CON,INT,WIS,VIT,SPI,LCK,WIL,CHA,PhysicalPower,SpellPower,Armor,MagicResistance,CritChance,CritDamage,Accuracy,Evasion,MaxHP,MaxMP,MaxStamina };
struct Modifier { Stat stat; double value; enum class Op { Add, AddPercent, Multiply, Override }; Op op{Op::Add}; int priority{}; };
class StatBlock { std::unordered_map<Stat,double> base_, effective_; std::vector<Modifier> mods_; public: void set(Stat s,double v){base_[s]=v;recalculate();} double get(Stat s)const{auto i=effective_.find(s);return i==effective_.end()?0.0:i->second;} void add(Modifier m){mods_.push_back(m);recalculate();} void recalculate(){effective_=base_;std::sort(mods_.begin(),mods_.end(),[](auto&a,auto&b){return a.priority<b.priority;});for(auto&m:mods_){auto&v=effective_[m.stat];switch(m.op){case Modifier::Op::Add:v+=m.value;break;case Modifier::Op::AddPercent:v+=v*m.value;break;case Modifier::Op::Multiply:v*=m.value;break;case Modifier::Op::Override:v=m.value;break;}}}};
}
