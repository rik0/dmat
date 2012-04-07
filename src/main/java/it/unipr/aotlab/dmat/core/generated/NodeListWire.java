// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: home/paolo/uni/dissertation/dmat/proto/NodeListWire.proto

package it.unipr.aotlab.dmat.core.generated;

public final class NodeListWire {
  private NodeListWire() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface NodeListBodyOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // repeated string nodeId = 1;
    java.util.List<String> getNodeIdList();
    int getNodeIdCount();
    String getNodeId(int index);
  }
  public static final class NodeListBody extends
      com.google.protobuf.GeneratedMessage
      implements NodeListBodyOrBuilder {
    // Use NodeListBody.newBuilder() to construct.
    private NodeListBody(Builder builder) {
      super(builder);
    }
    private NodeListBody(boolean noInit) {}
    
    private static final NodeListBody defaultInstance;
    public static NodeListBody getDefaultInstance() {
      return defaultInstance;
    }
    
    public NodeListBody getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return it.unipr.aotlab.dmat.core.generated.NodeListWire.internal_static_NodeListBody_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return it.unipr.aotlab.dmat.core.generated.NodeListWire.internal_static_NodeListBody_fieldAccessorTable;
    }
    
    // repeated string nodeId = 1;
    public static final int NODEID_FIELD_NUMBER = 1;
    private com.google.protobuf.LazyStringList nodeId_;
    public java.util.List<String>
        getNodeIdList() {
      return nodeId_;
    }
    public int getNodeIdCount() {
      return nodeId_.size();
    }
    public String getNodeId(int index) {
      return nodeId_.get(index);
    }
    
    private void initFields() {
      nodeId_ = com.google.protobuf.LazyStringArrayList.EMPTY;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      for (int i = 0; i < nodeId_.size(); i++) {
        output.writeBytes(1, nodeId_.getByteString(i));
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      {
        int dataSize = 0;
        for (int i = 0; i < nodeId_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeBytesSizeNoTag(nodeId_.getByteString(i));
        }
        size += dataSize;
        size += 1 * getNodeIdList().size();
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }
    
    public static it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBodyOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return it.unipr.aotlab.dmat.core.generated.NodeListWire.internal_static_NodeListBody_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return it.unipr.aotlab.dmat.core.generated.NodeListWire.internal_static_NodeListBody_fieldAccessorTable;
      }
      
      // Construct using it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        nodeId_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody.getDescriptor();
      }
      
      public it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody getDefaultInstanceForType() {
        return it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody.getDefaultInstance();
      }
      
      public it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody build() {
        it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody buildPartial() {
        it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody result = new it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody(this);
        int from_bitField0_ = bitField0_;
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          nodeId_ = new com.google.protobuf.UnmodifiableLazyStringList(
              nodeId_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.nodeId_ = nodeId_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody) {
          return mergeFrom((it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody other) {
        if (other == it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody.getDefaultInstance()) return this;
        if (!other.nodeId_.isEmpty()) {
          if (nodeId_.isEmpty()) {
            nodeId_ = other.nodeId_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureNodeIdIsMutable();
            nodeId_.addAll(other.nodeId_);
          }
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        return true;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder(
            this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              this.setUnknownFields(unknownFields.build());
              onChanged();
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                onChanged();
                return this;
              }
              break;
            }
            case 10: {
              ensureNodeIdIsMutable();
              nodeId_.add(input.readBytes());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // repeated string nodeId = 1;
      private com.google.protobuf.LazyStringList nodeId_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      private void ensureNodeIdIsMutable() {
        if (!((bitField0_ & 0x00000001) == 0x00000001)) {
          nodeId_ = new com.google.protobuf.LazyStringArrayList(nodeId_);
          bitField0_ |= 0x00000001;
         }
      }
      public java.util.List<String>
          getNodeIdList() {
        return java.util.Collections.unmodifiableList(nodeId_);
      }
      public int getNodeIdCount() {
        return nodeId_.size();
      }
      public String getNodeId(int index) {
        return nodeId_.get(index);
      }
      public Builder setNodeId(
          int index, String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureNodeIdIsMutable();
        nodeId_.set(index, value);
        onChanged();
        return this;
      }
      public Builder addNodeId(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureNodeIdIsMutable();
        nodeId_.add(value);
        onChanged();
        return this;
      }
      public Builder addAllNodeId(
          java.lang.Iterable<String> values) {
        ensureNodeIdIsMutable();
        super.addAll(values, nodeId_);
        onChanged();
        return this;
      }
      public Builder clearNodeId() {
        nodeId_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
        return this;
      }
      void addNodeId(com.google.protobuf.ByteString value) {
        ensureNodeIdIsMutable();
        nodeId_.add(value);
        onChanged();
      }
      
      // @@protoc_insertion_point(builder_scope:NodeListBody)
    }
    
    static {
      defaultInstance = new NodeListBody(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:NodeListBody)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_NodeListBody_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_NodeListBody_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n9home/paolo/uni/dissertation/dmat/proto" +
      "/NodeListWire.proto\"\036\n\014NodeListBody\022\016\n\006n" +
      "odeId\030\001 \003(\tB%\n#it.unipr.aotlab.dmat.core" +
      ".generated"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_NodeListBody_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_NodeListBody_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_NodeListBody_descriptor,
              new java.lang.String[] { "NodeId", },
              it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody.class,
              it.unipr.aotlab.dmat.core.generated.NodeListWire.NodeListBody.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}