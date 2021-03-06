package protocolsupport.protocol.packet.middleimpl.clientbound.play.v_pe;

import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.packet.middle.clientbound.play.MiddleSpawnObject;
import protocolsupport.protocol.packet.middleimpl.ClientBoundPacketData;
import protocolsupport.protocol.packet.middleimpl.clientbound.play.v_pe.EntityMetadata.PeMetaBase;
import protocolsupport.protocol.typeremapper.pe.PEDataValues;
import protocolsupport.protocol.utils.datawatcher.DataWatcherDeserializer;
import protocolsupport.protocol.utils.datawatcher.DataWatcherObject;
import protocolsupport.protocol.utils.datawatcher.objects.DataWatcherObjectSVarInt;
import protocolsupport.protocol.utils.minecraftdata.MinecraftData;
import protocolsupport.protocol.utils.types.networkentity.NetworkEntityItemDataCache;
import protocolsupport.utils.CollectionsUtils.ArrayMap;
import protocolsupport.utils.recyclable.RecyclableCollection;
import protocolsupport.utils.recyclable.RecyclableEmptyList;
import protocolsupport.utils.recyclable.RecyclableSingletonList;

public class SpawnObject extends MiddleSpawnObject {

	@Override
	public RecyclableCollection<ClientBoundPacketData> toData() {
		ProtocolVersion version = connection.getVersion();
		ArrayMap<DataWatcherObject<?>> spawnmeta = null;
		switch (entity.getType()) {
			case ITEM: {
				((NetworkEntityItemDataCache) entity.getDataCache()).setData(x, y, z, motX / 8000F, motY / 8000F, motZ / 8000F);
				return RecyclableEmptyList.get();
			}
			case ITEM_FRAME: {
				return RecyclableEmptyList.get();
			}
			case FALLING_OBJECT: {
				spawnmeta = new ArrayMap<>(DataWatcherDeserializer.MAX_USED_META_INDEX + 1);
				y -= 0.1; //Freaking PE pushes block because block breaks after sand is spawned
				spawnmeta.put(PeMetaBase.VARIANT, new DataWatcherObjectSVarInt(PEDataValues.BLOCK_ID.getRemap(MinecraftData.getBlockStateFromObjData(objectdata))));
			}
			default: {
				return RecyclableSingletonList.create(SpawnLiving.create(
					version, cache.getAttributesCache().getLocale(),
					entity,
					x, y, z,
					motX / 8.000F, motY / 8000.F, motZ / 8000.F,
					pitch, yaw, 0,
					spawnmeta
				));
			}
		}
	}

}
